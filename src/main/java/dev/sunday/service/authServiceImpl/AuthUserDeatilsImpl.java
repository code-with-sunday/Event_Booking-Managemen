package dev.sunday.service.authServiceImpl;

import dev.sunday.DTO.request.LoginRequestDTO;
import dev.sunday.DTO.response.AuthResponse;
import dev.sunday.enums.ROLE;
import dev.sunday.model.User;
import dev.sunday.repository.UserRepository;
import dev.sunday.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthUserDeatilsImpl implements AuthUserDetails{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsImpl customUserDetails;



    @Override
    public AuthResponse createUserHandler(User user) throws Exception {
        User isEmailExist  = userRepository.findByEmail(user.getEmail());

        if(isEmailExist != null){
            throw new Exception("Email is already used with another account");
        }

        User createUser = new User();
        createUser.setEmail(user.getEmail());
        createUser.setName(user.getName());
        createUser.setRole(user.getRole());
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(createUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setTitle("Welcome " + user.getEmail());
        authResponse.setMessage("Register success");
        authResponse.setRole(savedUser.getRole());

        return authResponse;
    }

    @Override
    public AuthResponse signIn(LoginRequestDTO loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Login success");
        authResponse.setTitle(jwt);
        authResponse.setRole(ROLE.valueOf(role));

        return authResponse;
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        if(userDetails == null){
            throw new BadCredentialsException("Invalid username...");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password....");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
    }
}
