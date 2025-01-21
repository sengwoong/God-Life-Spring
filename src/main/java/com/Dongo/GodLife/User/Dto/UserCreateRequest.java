package com.Dongo.GodLife.User.Dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    private String email;
    private String password;

    @Size(min = 3, max = 8, message = "닉네임은 3글자 이상 8글자 이하여야 합니다.")
    private String nickName;
}
