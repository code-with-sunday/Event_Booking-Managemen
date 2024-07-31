package dev.sunday.controller;

import dev.sunday.DTO.request.LoginRequestDTO;
import dev.sunday.DTO.request.UserDTO;
import dev.sunday.DTO.response.AuthResponse;
import dev.sunday.model.User;
import dev.sunday.service.UserService;
import dev.sunday.service.authServiceImpl.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthUserDetails authUserDetails;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody UserDTO userDTO) throws Exception {
        return ResponseEntity.ok(authUserDetails.createUserHandler(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequestDTO loginRequest){
        return ResponseEntity.ok(authUserDetails.signIn(loginRequest));
    }



}
