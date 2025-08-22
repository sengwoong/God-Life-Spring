package com.Dongo.GodLife.User.Dto;

import com.Dongo.GodLife.User.Model.JwtToken;
import com.Dongo.GodLife.User.Model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private User user;
    private JwtToken jwtToken;
}
