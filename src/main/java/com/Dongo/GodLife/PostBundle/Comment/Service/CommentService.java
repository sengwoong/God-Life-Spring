package com.Dongo.GodLife.PostBundle.Comment.Service;

import com.Dongo.GodLife.PostBundle.Comment.Dto.CommentRequest;
import com.Dongo.GodLife.PostBundle.Comment.Model.Comment;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Post.Service.PostService;
import com.Dongo.GodLife.User.Model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentPersistenceAdapter commentPersistenceAdapter;
    private final PostService postService;
    
    public Comment createComment(CommentRequest request, User user) {
        Post post = postService.findSharedById(request.getPostId());
        
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setPost(post);
        comment.setUser(user);
        
        Comment savedComment = commentPersistenceAdapter.save(comment);
        
        // 게시물의 댓글 수 업데이트
        Long commentCount = commentPersistenceAdapter.countByPostId(request.getPostId());
        postService.updateCommentCount(request.getPostId(), commentCount.intValue());
        
        return savedComment;
    }
    
    public Comment findById(Long commentId) {
        return commentPersistenceAdapter.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다"));
    }
    
    public Page<Comment> getCommentsByPost(Long postId, Pageable pageable) {
        return commentPersistenceAdapter.findByPostId(postId, pageable);
    }
    
    public Page<Comment> getCommentsByUser(User user, Pageable pageable) {
        return commentPersistenceAdapter.findByUser(user, pageable);
    }
    
    public Long getCommentCountByPost(Long postId) {
        return commentPersistenceAdapter.countByPostId(postId);
    }
    
    public Comment updateComment(Long commentId, CommentRequest request, User user) {
        Comment comment = findById(commentId);
        
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("댓글을 수정할 권한이 없습니다");
        }
        
        comment.setContent(request.getContent());
        return commentPersistenceAdapter.save(comment);
    }
    
    public void deleteComment(Long commentId, User user) {
        Comment comment = findById(commentId);
        
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("댓글을 삭제할 권한이 없습니다");
        }
        
        Long postId = comment.getPost().getId();
        commentPersistenceAdapter.delete(comment);
        
        // 게시물의 댓글 수 업데이트
        Long commentCount = commentPersistenceAdapter.countByPostId(postId);
        postService.updateCommentCount(postId, commentCount.intValue());
    }
} 