package com.Dongo.GodLife.User.Dto;

import com.Dongo.GodLife.User.Model.User;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {
    @Size(min = 3, max = 8, message = "닉네임은 3글자 이상 8글자 이하여야 합니다")
    private String nickName;
    
    @Pattern(regexp = "^01[0-9]-[0-9]{4}-[0-9]{4}$", message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다")
    private String phoneNumber;
    
    @Size(max = 200, message = "주소는 200자를 초과할 수 없습니다")
    private String address;
    
    @Email(message = "유효한 이메일 형식이어야 합니다")
    private String email;

    public UpdateUserRequest(User user) {
        this.nickName = user.getNickName();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.email = user.getEmail();
    }

}