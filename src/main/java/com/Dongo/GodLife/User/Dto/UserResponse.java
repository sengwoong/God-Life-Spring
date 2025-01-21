package com.example.GodLife.User.Dto;

import com.example.GodLife.MusicBundle.PlayList.Model.Playlist;
import com.example.GodLife.ProductBundle.Post.Model.Post;
import com.example.GodLife.ProductBundle.Product.Model.Product;
import com.example.GodLife.ScheduleBundle.Schedule.Model.Schedule;
import com.example.GodLife.User.Model.User;
import com.example.GodLife.VocaBundle.Voca.Model.Voca;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserResponse {
    private Long Id;
    private String nickName;
    private int sales;
    private String phoneNumber;
    private String address;
    private String email;
    private LocalDateTime createdAt;
    private List<Voca> vocas;
    private List<Schedule> schedules;
    private List<Playlist> playlists;
    private List<Post> posts;
    private List<Product> orders;

    public UserResponse(User user) {
        this.Id = user.getId();
        this.nickName = user.getNickName();
        this.sales = user.getSales();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.vocas = user.getVoca();
        this.schedules = user.getSchedules();
        this.playlists = user.getPlaylists();
        this.posts = user.getPosts();
        this.orders = user.getProduct();
    }
}