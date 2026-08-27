package com.parking_reservation_system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.parking_reservation_system.dto.request.EmailVerificationRequest;
import com.parking_reservation_system.dto.request.LoginUserRequest;
import com.parking_reservation_system.dto.request.RegisterUserRequest;
import com.parking_reservation_system.dto.request.ResetPasswordRequest;
import com.parking_reservation_system.dto.response.EmailVerificationResponse;
import com.parking_reservation_system.dto.response.RegisterUserResponse;
import com.parking_reservation_system.exception.UserAlreadyExistedException;
import com.parking_reservation_system.model.PasswordResetToken;
import com.parking_reservation_system.model.Role;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.repository.PasswordResetRepository;
import com.parking_reservation_system.repository.UserRepository;
import com.parking_reservation_system.utils.HashUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks private AuthenticationService authService;

    @Mock private UserRepository userRepository;

    @Mock private PasswordEncoder passwordEncoder;

    @Mock private JWTService jwtService;

    @Mock private EmailService emailService;

    @Mock private AuthenticationManager authManager;

    @Mock private PasswordResetRepository passwordResetRepository;

    @Test
    void registerUser_successful() {
    RegisterUserDto dto = new RegisterUserDto(
            null, "Mido", "test@gmail.com", "123", "01001111111", List.of(new Role("USER")));

    when(userRepository.existsByEmail(dto.email())).thenReturn(false);
    when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class)))
            .thenAnswer(invocation -> {
                User u = invocation.getArgument(0);
                u.setId(1);
                return u;
            });

    RegisterUserResponseDto response = authService.RegisterUser(dto);

    // response contract
    assertThat(response.id()).isEqualTo(1);
    assertThat(response.email()).isEqualTo(dto.email());
    assertThat(response.name()).isEqualTo(dto.name());
    assertThat(response.phone()).isEqualTo(dto.phone());
    assertThat(response.roles()).isEqualTo(dto.roles());

        // saved user state
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertThat(savedUser.isVerified()).isFalse();
        assertThat(savedUser.getExpirationTime())
                .isAfter(LocalDateTime.now().plusMinutes(14))
                .isBefore(LocalDateTime.now().plusMinutes(16));

        verify(emailService)
                .sendVerificationEmail(eq(dto.email()) , codeCaptor.capture());
        String sentCode = codeCaptor.getValue();     
      
        assertThat(savedUser.getVerificationCode())
        .isEqualTo(HashUtils.hashVerificationCode(sentCode));   
    }

    @Test
    void registerUser_emailAlreadyExists_throwsException() {
        RegisterUserDto dto =
                new RegisterUserDto(
                        null,
                        "Mido",
                        "test@gmail.com",
                        "123",
                        "01001111111",
                        List.of(new Role("USER")));

        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(UserAlreadyExistedException.class, () -> authService.registerUser(dto));

        verify(userRepository, never()).save(any(User.class));
        verify(emailService, never()).sendVerificationEmail(anyString(), anyString());
    }

    @Test
    void verifyUser_successful() {
        String email = "test@gmail.com";
        String ExistedCode = "123456";

        EmailVerificationDto dto = new EmailVerificationDto(ExistedCode, email);

        User user = new User();
        user.setId(1);
        user.setEmail(email);
        user.setVerificationCode(HashUtils.hashVerificationCode(ExistedCode));
        user.setExpirationTime(LocalDateTime.now().plusMinutes(15));
        user.setVerified(false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EmailVerificationResponseDto response = authService.verifyUser(dto);

        assertTrue(user.isVerified());
        assertEquals(email, response.email());
        assertEquals(ExistedCode, response.verificationCode());

        verify(userRepository).save(user);
    }

    @Test
    void verifyUser_invalidVerificationCode_throwsException() {
        String email = "test@gmail.com";
        String inputWrongCode = "99999";

        EmailVerificationDto dto = new EmailVerificationDto(inputWrongCode, email);

        User testUser = new User();
        testUser.setEmail(email);
        testUser.setVerificationCode("12345");
        testUser.setExpirationTime(LocalDateTime.now().plusMinutes(15));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        assertThrows(RuntimeException.class, () -> authService.verifyUser(dto));

        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void verifyUser_expiredVerificationCode_throwsException() {
        String email = "test@gmail.com";
        String code = "12345";
        EmailVerificationDto dto = new EmailVerificationDto(code, email);

        User testUser = new User();
        testUser.setEmail(email);
        testUser.setVerificationCode(code);
        testUser.setExpirationTime(LocalDateTime.now().minusMinutes(15)); // Expired

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        assertThrows(RuntimeException.class, () -> authService.verifyUser(dto));

        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void verifyUser_userNotFound_throwsException() {
        String email = "notfound@gmail.com";
        String code = "12345";
        EmailVerificationDto dto = new EmailVerificationDto(code, email);
        when(userRepository.findByEmail(email)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> authService.verifyUser(dto));
        verify(userRepository).findByEmail(email);
    }

    @Test
    void createResetToken_successful() {
        // Given
        User user = new User();
        user.setId(1);
        user.setEmail("test@gmail.com");

        when(passwordResetRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(passwordResetRepository.save(any(PasswordResetToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        PasswordResetToken token = authService.createResetToken(user);

        // Then
        assertThat(token).isNotNull();
        assertThat(token.getToken()).isNotEmpty();
        assertThat(token.getExpiryDate()).isAfter(LocalDateTime.now());
        assertThat(token.getUser()).isEqualTo(user);

        verify(passwordResetRepository).findByUserId(user.getId());
        verify(passwordResetRepository).save(any(PasswordResetToken.class));
    }

    @Test
    void loginUser_successful() {
        // Arrange
        String email = "test@gmail.com";
        String password = "123";

        LoginUserDto dto = new LoginUserDto(null, email, password);
        User user = new User();
        user.setId(1);
        user.setEmail(email);
        user.setPassword("encodedPassword");
        user.setVerified(true);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(jwtService.generateToken(user.getEmail())).thenReturn("jwt-token");

        // Act
        String token = authService.loginUser(dto);

        // Assert
        assertThat(token).isEqualTo("jwt-token");

        verify(userRepository).findByEmail(email);
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(user.getEmail());
    }

    @Test
    void loginUser_unverifiedAccount_throwsException() {
        // Given
        String email = "test@gmail.com";
        String password = "123";
        LoginUserDto dto = new LoginUserDto(null, email, password);

        User user = new User();
        user.setEmail(email);
        user.setVerified(false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.loginUser(dto));

        verify(userRepository).findByEmail(email);
        verify(authManager, never()).authenticate(any());
    }

    @Test
    void loginUser_invalidCredentials_throwsException() {
        // Given
        String email = "test@gmail.com";
        String password = "wrongpassword";
        LoginUserDto loginUserDto = new LoginUserDto(null, email, password);
        User user = new User();
        user.setEmail(email);
        user.setVerified(true);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When & Then
        assertThrows(BadCredentialsException.class, () -> authService.loginUser(loginUserDto));

        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void resetPassword_successful() {
        // Given
        String resetPasswordcode = "123456";
        String email = "test@gmail.com";
        String newPassword = "newPassword123";
        ResetPasswordRequest ResetPasswordRequest =
                new ResetPasswordRequest(resetPasswordcode , email, newPassword, newPassword);

        User user = new User();
        user.setId(1);
        user.setEmail(email);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(resetPasswordcode);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordResetRepository.findByUserId(1)).thenReturn(Optional.of(resetToken));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        // When
        authService.resetPassword(ResetPasswordRequest);

        // Then
        verify(userRepository).findByEmail(email);
        verify(passwordResetRepository).findByUserId(1);
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(argThat(u -> u.getPassword().equals("encodedNewPassword")));
        verify(passwordResetRepository).delete(resetToken);
    }

    @Test
    void resetPassword_expiredToken_throwsException() {
        // Given
        String resetPasswordcode = "123456";
        String email = "test@gmail.com";
        String newPassword = "newPassword123";
        ResetPasswordRequest ResetPasswordRequest =
                new ResetPasswordRequest(resetPasswordcode , email, newPassword, newPassword);

        User user = new User();
        user.setId(1);
        user.setEmail(email);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(resetPasswordcode);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().minusHours(1)); // Expired

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordResetRepository.findByToken(resetPasswordcode)).thenReturn(Optional.of(resetToken));

        // When & Then
        assertThrows(
                RuntimeException.class,
                () -> authService.resetPassword(ResetPasswordRequest));

        verify(userRepository, never()).save(any(User.class));
        verify(passwordResetRepository, never()).delete(any());
    }

    @Test
    void resetPassword_invalidToken_throwsException() {
        // Given
        String resetPasswordcode = "123456";
        String email = "test@gmail.com";
        String token = "invalid-token";
        String newPassword = "newPassword123";
        ResetPasswordRequest ResetPasswordRequest =
                new ResetPasswordRequest(resetPasswordcode , email, newPassword, newPassword);

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordResetRepository.findByToken(token)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
                RuntimeException.class,
                () -> authService.resetPassword(ResetPasswordRequest));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void sendVerificationEmail_doesNotSendRealEmail() {
        // Given
        RegisterUserDto dto =
                new RegisterUserDto(
                        null,
                        "Mido",
                        "test@gmail.com",
                        "123",
                        "01001111111",
                        List.of(new Role("USER")));

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenAnswer(
                        invocation -> {
                            User u = invocation.getArgument(0);
                            u.setId(1);
                            return u;
                        });

        // When
        authService.RegisterUser(dto);

        // Then
        verify(emailService).sendVerificationEmail(eq(dto.email()), anyString());

    }
}
