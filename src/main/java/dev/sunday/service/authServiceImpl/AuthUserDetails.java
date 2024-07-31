package dev.sunday.service.authServiceImpl;

import dev.sunday.DTO.request.LoginRequestDTO;
import dev.sunday.DTO.request.UserDTO;
import dev.sunday.DTO.response.AuthResponse;
import dev.sunday.model.User;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthUserDetails {
    AuthResponse createUserHandler(UserDTO userDTO) throws Exception;

    AuthResponse signIn(@RequestBody LoginRequestDTO loginRequest);
}

