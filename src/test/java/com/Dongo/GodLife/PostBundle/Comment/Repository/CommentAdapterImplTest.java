package com.Dongo.GodLife.PostBundle.Comment.Repository;

import com.Dongo.GodLife.PostBundle.Comment.Model.Comment;
import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.User.Model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentAdapterImplTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentAdapterImpl commentAdapter;

    @Test
    @DisplayName("save/findById 위임")
    void saveAndFindById() {
        Comment c = Comment.builder().id(1L).build();
        given(commentRepository.save(c)).willReturn(c);
        given(commentRepository.findById(1L)).willReturn(Optional.of(c));

        assertEquals(1L, commentAdapter.save(c).getId());
        assertTrue(commentAdapter.findById(1L).isPresent());
        verify(commentRepository).save(c);
        verify(commentRepository).findById(1L);
    }

    @Test
    @DisplayName("페이지 조회 위임: findByPost/findByUser/findByPostId")
    void pageQueries() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Comment> page = new PageImpl<>(List.of(Comment.builder().id(1L).build()));
        Post post = Post.builder().id(7L).build();
        User user = User.builder().id(8L).build();

        given(commentRepository.findByPost(post, pageable)).willReturn(page);
        given(commentRepository.findByUser(user, pageable)).willReturn(page);
        given(commentRepository.findByPostId(7L, pageable)).willReturn(page);

        assertEquals(1, commentAdapter.findByPost(post, pageable).getTotalElements());
        assertEquals(1, commentAdapter.findByUser(user, pageable).getTotalElements());
        assertEquals(1, commentAdapter.findByPostId(7L, pageable).getTotalElements());
    }

    @Test
    @DisplayName("countByPostId/delete 위임")
    void countAndDelete() {
        given(commentRepository.countByPostId(3L)).willReturn(5L);
        assertEquals(5L, commentAdapter.countByPostId(3L));

        Comment c = Comment.builder().id(2L).build();
        commentAdapter.delete(c);
        verify(commentRepository).delete(c);
    }
}


