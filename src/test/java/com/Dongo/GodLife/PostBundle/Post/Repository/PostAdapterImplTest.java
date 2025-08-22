package com.Dongo.GodLife.PostBundle.Post.Repository;

import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.Post.Model.Post.PostType;
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
class PostAdapterImplTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostAdapterImpl postAdapter;

    @Test
    @DisplayName("save/findById 위임")
    void saveAndFindById() {
        Post p = Post.builder().id(1L).build();
        given(postRepository.save(p)).willReturn(p);
        given(postRepository.findById(1L)).willReturn(Optional.of(p));

        assertEquals(1L, postAdapter.save(p).getId());
        assertTrue(postAdapter.findById(1L).isPresent());
        verify(postRepository).save(p);
        verify(postRepository).findById(1L);
    }

    @Test
    @DisplayName("도메인별 조회 위임: findByUserIdAndPostId/findByIsSharedTrueAndId")
    void findCompositeQueries() {
        given(postRepository.findByUserIdAndPostId(1L, 2L)).willReturn(Post.builder().id(2L).build());
        given(postRepository.findByIsSharedTrueAndId(3L)).willReturn(Post.builder().id(3L).build());

        assertEquals(2L, postAdapter.findByUserIdAndPostId(1L, 2L).get().getId());
        assertEquals(3L, postAdapter.findByIsSharedTrueAndId(3L).get().getId());
    }

    @Test
    @DisplayName("페이지 조회 위임들")
    void pageQueries() {
        PageRequest pageable = PageRequest.of(0, 10);
        User user = User.builder().id(1L).build();
        Page<Post> page = new PageImpl<>(List.of(Post.builder().id(1L).build()));

        given(postRepository.findByUser(user, pageable)).willReturn(page);
        given(postRepository.findByUserAndType(user, PostType.NORMAL, pageable)).willReturn(page);
        given(postRepository.findByIsSharedTrue(pageable)).willReturn(page);
        given(postRepository.findByIsAdTrue(pageable)).willReturn(page);
        given(postRepository.findSharedPostsByUser(user, pageable)).willReturn(page);
        given(postRepository.findAdPostsByUser(user, pageable)).willReturn(page);
        given(postRepository.findBySearch("a", pageable)).willReturn(page);
        given(postRepository.findByTypeAndSearch(PostType.NORMAL, "a", pageable)).willReturn(page);
        given(postRepository.findByTypeAndSearchAndIsSharedTrue(PostType.NORMAL, "a", pageable)).willReturn(page);
        given(postRepository.findByIsAdTrueAndIsSharedTrue(pageable)).willReturn(page);
        given(postRepository.findAdPostsByUserAndIsSharedTrue(user, pageable)).willReturn(page);
        given(postRepository.findBySearchAndIsSharedTrue("a", pageable)).willReturn(page);

        assertEquals(1, postAdapter.findByUser(user, pageable).getTotalElements());
        assertEquals(1, postAdapter.findByUserAndType(user, PostType.NORMAL, pageable).getTotalElements());
        assertEquals(1, postAdapter.findByIsSharedTrue(pageable).getTotalElements());
        assertEquals(1, postAdapter.findByIsAdTrue(pageable).getTotalElements());
        assertEquals(1, postAdapter.findSharedPostsByUser(user, pageable).getTotalElements());
        assertEquals(1, postAdapter.findAdPostsByUser(user, pageable).getTotalElements());
        assertEquals(1, postAdapter.findBySearch("a", pageable).getTotalElements());
        assertEquals(1, postAdapter.findByTypeAndSearch(PostType.NORMAL, "a", pageable).getTotalElements());
        assertEquals(1, postAdapter.findByTypeAndSearchAndIsSharedTrue(PostType.NORMAL, "a", pageable).getTotalElements());
        assertEquals(1, postAdapter.findByIsAdTrueAndIsSharedTrue(pageable).getTotalElements());
        assertEquals(1, postAdapter.findAdPostsByUserAndIsSharedTrue(user, pageable).getTotalElements());
        assertEquals(1, postAdapter.findBySearchAndIsSharedTrue("a", pageable).getTotalElements());
    }

    @Test
    @DisplayName("delete 위임")
    void deleteDelegation() {
        Post p = Post.builder().id(9L).build();
        postAdapter.delete(p);
        verify(postRepository).delete(p);
    }
}


