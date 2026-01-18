package com.parking_reservation_system.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.parking_reservation_system.dto.request.EmailVerificationDto;
import com.parking_reservation_system.dto.request.RegisterUserDto;
import com.parking_reservation_system.dto.request.ResetPasswordDto;
import com.parking_reservation_system.dto.response.EmailVerificationResponseDto;
import com.parking_reservation_system.dto.response.RegisterUserResponseDto;
import com.parking_reservation_system.model.Role;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.model.PasswordResetToken;
import com.parking_reservation_system.repository.PasswordResetRepository;
import com.parking_reservation_system.repository.UserRepository;


class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @Mock
    private EmailService emailService;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private PasswordResetRepository passwordResetRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_successful() {
        // Given
        RegisterUserDto dto = new RegisterUserDto(
            null ,
            "Mido",
            "test@gmail.com",
            "123",
            "01001111111",
            List.of(new Role("USER"))
        );

        /// like in math we say let existsByemail = false 
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        /// let passwordEncoder.encode() = encodedPassword
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
        /// let userRepository save user u with id = 1 and return the u
        when(userRepository.save(any(User.class)))
            .thenAnswer(invocation -> {
                User u = invocation.getArgument(0);
                u.setId(1);
                return u;
            });

        /// When : this logic called mockito will subsitute with the above lines of code
        /// which  represents a happy path on purpose 
        RegisterUserResponseDto response = authService.RegisterUser(dto);

        // Then
        // check the returned values meets the right standards
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1);
        assertThat(response.email()).isEqualTo(dto.email());
        assertThat(response.name()).isEqualTo(dto.name());
        /// verify using mockito that every logic every compnent is called without exceptions or errors
        verify(userRepository).existsByEmail(dto.email());
        verify(passwordEncoder).encode(dto.password());
        verify(userRepository).save(any(User.class));
            verify(emailService).sendVerificationEmail(eq(dto.email()), anyString());
    }

    @Test
    void registerUser_emailAlreadyExists_throwsException() {
        // Given
        RegisterUserDto dto = new RegisterUserDto(
            null ,
            "Mido",
            "test@gmail.com",
            "123",
            "01001111111",
            List.of(new Role("USER"))
        );
        
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.RegisterUser(dto));
        
        verify(userRepository).existsByEmail(dto.email());
        verify(userRepository, never()).save(any(User.class));
        verify(emailService, never()).sendVerificationEmail(anyString(), anyString());
    }


@Test
void verifyUser_successful() {
    // Given
    String email = "test@gmail.com";
    String ExistedCode = "12345";

    EmailVerificationDto dto = new EmailVerificationDto(ExistedCode, email);

    User user = new User();
    user.setId(1);
    user.setEmail(email);
    user.setVerificationCode(ExistedCode);
    user.setExpirationTime(LocalDateTime.now().plusMinutes(15));
    user.setVerified(false);

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // When
    EmailVerificationResponseDto response = authService.verifyUser(dto);

    // Then
    assertTrue(user.isVerified());
    assertEquals(email, response.email());
    assertEquals(ExistedCode, response.verificationCode());

    verify(userRepository).save(user);
}

    @Test
    void verifyUser_invalidVerificationCode_throwsException() {
        // Given
        String email = "test@gmail.com";
        String inputWrongCode = "99999";
        
        EmailVerificationDto dto = new EmailVerificationDto(inputWrongCode, email);

        User testUser = new User();
        testUser.setEmail(email);
        testUser.setVerificationCode("12345");
        testUser.setExpirationTime(LocalDateTime.now().plusMinutes(15));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.verifyUser(dto));
        
        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void verifyUser_expiredVerificationCode_throwsException() {
        // Given
        String email = "test@gmail.com";
        String code = "12345";
        EmailVerificationDto dto = new EmailVerificationDto(code, email);

        User testUser = new User();
        testUser.setEmail(email);
        testUser.setVerificationCode(code);
        testUser.setExpirationTime(LocalDateTime.now().minusMinutes(15)); // Expired

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.verifyUser(dto));
        
        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void verifyUser_userNotFound_throwsException() {
        // Given
        String email = "notfound@gmail.com";
        String code = "12345";
        EmailVerificationDto dto = new EmailVerificationDto(code, email);

        when(userRepository.findByEmail(email)).thenReturn(null);

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.verifyUser(dto));
        
        verify(userRepository).findByEmail(email);
    }
/* 
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
    void createResetToken_deletesExistingToken() {
        // Given
        User user = new User();
        user.setId(1);
        
        PasswordResetToken existingToken = new PasswordResetToken();
        existingToken.setId(100);

        when(passwordResetRepository.findByUserId(user.getId()))
            .thenReturn(Optional.of(existingToken));
        when(passwordResetRepository.save(any(PasswordResetToken.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        PasswordResetToken token = authService.createResetToken(user);

        // Then
        assertThat(token).isNotNull();
        verify(passwordResetRepository).delete(existingToken);
        verify(passwordResetRepository).save(any(PasswordResetToken.class));
    }

    @Test
    void loginUser_successful() {
        // Given
        String email = "test@gmail.com";
        String password = "123";
        
        User user = new User();
        user.setId(1);
        user.setEmail(email);
        user.setPassword("encodedPassword");
        user.setVerified(true);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(null);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        // When
        String token = authService.loginUser(email, password);

        // Then
        assertThat(token).isEqualTo("jwt-token");
        
        verify(userRepository).findByEmail(email);
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(user);
    }

    @Test
    void loginUser_unverifiedAccount_throwsException() {
        // Given
        String email = "test@gmail.com";
        String password = "123";
        
        User user = new User();
        user.setEmail(email);
        user.setVerified(false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.loginUser(email, password));
        
        verify(userRepository).findByEmail(email);
        verify(authManager, never()).authenticate(any());
    }

    @Test
    void loginUser_invalidCredentials_throwsException() {
        // Given
        String email = "test@gmail.com";
        String password = "wrongpassword";
        
        User user = new User();
        user.setEmail(email);
        user.setVerified(true);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When & Then
        assertThrows(BadCredentialsException.class, () -> authService.loginUser(email, password));
        
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void resetPassword_successful() {
        // Given
        String email = "test@gmail.com";
        String token = "reset-token";
        String newPassword = "newPassword123";
        
        User user = new User();
        user.setId(1);
        user.setEmail(email);
        
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordResetRepository.findByToken(token)).thenReturn(Optional.of(resetToken));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        // When
        authService.resetPassword(ResetPasswordDto, token);

        // Then
        verify(userRepository).findByEmail(email);
        verify(passwordResetRepository).findByToken(token);
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(argThat(u -> u.getPassword().equals("encodedNewPassword")));
        verify(passwordResetRepository).delete(resetToken);
    }

    @Test
    void resetPassword_expiredToken_throwsException() {
        // Given
        String email = "test@gmail.com";
        String token = "expired-token";
        String newPassword = "newPassword123";
        
        User user = new User();
        user.setId(1);
        user.setEmail(email);
        
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().minusHours(1)); // Expired

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordResetRepository.findByToken(token)).thenReturn(Optional.of(resetToken));

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            authService.resetPassword(email, token, newPassword));
        
        verify(userRepository, never()).save(any(User.class));
        verify(passwordResetRepository, never()).delete(any());
    }

    @Test
    void resetPassword_invalidToken_throwsException() {
        // Given
        String email = "test@gmail.com";
        String token = "invalid-token";
        String newPassword = "newPassword123";
        
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordResetRepository.findByToken(token)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            authService.resetPassword(email, token, newPassword));
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void sendVerificationEmail_doesNotSendRealEmail() {
        // Given
        RegisterUserDto dto = new RegisterUserDto(
            "Mido",
            "test@gmail.com",
            "123",
            "01001111111",
            List.of(new Role("USER"))
        );

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
            .thenAnswer(invocation -> {
                User u = invocation.getArgument(0);
                u.setId(1);
                return u;
            });

        // When
        authService.RegisterUser(dto);

        // Then
        // Verify that emailService.sendVerificationEmail was called
        // but because emailService is mocked, no real email is sent
        verify(emailService).sendVerificationEmail(eq(dto.email()), anyString());
        
        // You can also verify it was called exactly once
        verify(emailService, times(1)).sendVerificationEmail(anyString(), anyString());
    }


    */
}