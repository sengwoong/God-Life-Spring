package com.Dongo.GodLife.User.Dto;

import com.Dongo.GodLife.User.Model.User;
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


    public UserResponse(User user) {
        this.Id = user.getId();
        this.nickName = user.getNickName();
        this.sales = user.getSales();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();

    }
}