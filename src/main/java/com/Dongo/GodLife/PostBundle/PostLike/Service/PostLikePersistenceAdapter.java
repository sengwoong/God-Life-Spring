package com.Dongo.GodLife.PostBundle.PostLike.Service;

import com.Dongo.GodLife.PostBundle.Post.Model.Post;
import com.Dongo.GodLife.PostBundle.PostLike.Model.PostLike;
import com.Dongo.GodLife.User.Model.User;

public interface PostLikePersistenceAdapter {
    PostLike save(PostLike postLike);
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
} 