package com.Dongo.GodLife.User.Dto;

import com.Dongo.GodLife.User.Model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {
    private String nickName;
    private String phoneNumber;
    private String address;
    private String email;

    public UpdateUserRequest(User user) {
        this.nickName = user.getNickName();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.email = user.getEmail();
    }

}