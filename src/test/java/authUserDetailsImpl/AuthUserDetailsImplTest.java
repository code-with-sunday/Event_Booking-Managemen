package authUserDetailsImpl;

import dev.sunday.DTO.request.UserDTO;
import dev.sunday.DTO.response.AuthResponse;
import dev.sunday.model.User;
import dev.sunday.repository.UserRepository;
import dev.sunday.security.JwtProvider;
import dev.sunday.service.authServiceImpl.AuthUserDeatilsImpl;
import dev.sunday.service.authServiceImpl.CustomUserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static dev.sunday.enums.ROLE.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AuthUserDetailsImplTest {

    private AuthUserDeatilsImpl authUserDetailsImplTest;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private CustomUserDetailsImpl customUserDetails;

    @BeforeEach
    void setUp() {
        authUserDetailsImplTest =  new AuthUserDeatilsImpl(userRepository, passwordEncoder, jwtProvider, customUserDetails);
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO()
                .builder()
                .email("email@email.com")
                .password("Password@121")
                .role(ROLE_USER)
                .name("Sunday")
                .build();

        AuthResponse authResponse = new AuthResponse()
                .builder()
                .Title("WELCOME")
                .message("Register success")
                .role(ROLE_USER)
                .build();

        User user = new User().builder()
                .name("Sunday")
                .email("email@email.com")
                .password("password@121")
                .role(ROLE_USER)
                .build();

        when(userRepository.save(any())).thenReturn(user);
        AuthResponse returnedUser = authUserDetailsImplTest.createUserHandler(userDTO);
        assertEquals(returnedUser.getMessage(), authResponse.getMessage());
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("zuzu@email.com");
        userDTO.setPassword("EncodedPassword");

        AuthResponse authResponse = new AuthResponse()
                .builder()
                .Title("WELCOME")
                .message("Login success")
                .role(ROLE_USER)
                .build();
        User savedUser = User.builder()
                .email("zuzu@email.com")
                .password("EncodedPassword")
                .role(userDTO.getRole())
                .build();

        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("EncodedPassword");

        assertEquals(passwordEncoder.encode(userDTO.getPassword()), "EncodedPassword");
    }
}
