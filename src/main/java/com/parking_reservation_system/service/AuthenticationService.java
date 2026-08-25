package com.parking_reservation_system.service;

import com.parking_reservation_system.dto.request.EmailVerificationRequest;
import com.parking_reservation_system.dto.request.LoginUserRequest;
import com.parking_reservation_system.dto.request.RegisterUserRequest;
import com.parking_reservation_system.dto.request.ResetPasswordRequest;
import com.parking_reservation_system.dto.response.EmailVerificationResponse;
import com.parking_reservation_system.dto.response.RegisterUserResponse;
import com.parking_reservation_system.exception.AccountNotVerifiedException;
import com.parking_reservation_system.exception.InvalidVerificationCodeException;
import com.parking_reservation_system.exception.ResourceNotFoundException;
import com.parking_reservation_system.exception.SamePasswordException;
import com.parking_reservation_system.exception.TokenExpiredException;
import com.parking_reservation_system.exception.TokenMismatchException;
import com.parking_reservation_system.exception.UserAlreadyExistedException;
import com.parking_reservation_system.mapper.UserMapper;
import com.parking_reservation_system.model.PasswordResetToken;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.repository.PasswordResetRepository;
import com.parking_reservation_system.repository.UserRepository;
import com.parking_reservation_system.utils.HashUtils;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authManager;
    private final PasswordResetRepository passwordResetRepository;
    private final EmailService emailService;

    private static final SecureRandom RANDOM = new SecureRandom();

    @Transactional
    public RegisterUserResponse registerUser(RegisterUserRequest userDto) {
        boolean isEmailExist = userRepository.existsByEmail(userDto.email());
        if (isEmailExist) {
            throw new UserAlreadyExistedException("User already registered");
        }

        String code = String.format("%06d", RANDOM.nextInt(100_000));

        User user = UserMapper.toUser(userDto, passwordEncoder);
        user.setVerificationCode(HashUtils.hashVerificationCode(code));
        user.setVerified(false);
        user.setExpirationTime(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        emailService.sendVerificationEmail(userDto.email(), code);

        return new RegisterUserResponse(
                user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRoles());
    }

    public EmailVerificationResponse verifyUser(EmailVerificationRequest dto)
            throws RuntimeException {
        User existingUser =
                userRepository
                        .findByEmail(dto.email())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (existingUser.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code has expired");
        }

        if (existingUser.getVerificationCode().equals(dto.verificationCode())) {
            existingUser.setVerified(true);
            userRepository.save(existingUser);
        } else {
            throw new InvalidVerificationCodeException("Invalid verification code");
        }
        return new EmailVerificationResponse(dto.verificationCode(), dto.email());
    }

    @Transactional
    public void resendVerificationCode(String email) throws RuntimeException {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        if (user.isVerified()) {
            throw new RuntimeException("User is already verified");
        }
        String code = String.format("%06d", RANDOM.nextInt(100_000));

        user.setVerificationCode(code);
        user.setExpirationTime(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);
        emailService.sendVerificationEmail(email, code);
    }

    public String loginUser(LoginUserRequest userDto) {

        User registeredUser =
                userRepository
                        .findByEmail(userDto.email())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!registeredUser.isVerified())
            throw new AccountNotVerifiedException("account has not verifiyed yet");

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.email(), userDto.password()));
        return jwtService.generateToken(userDto.email());
    }

    @Transactional
    public PasswordResetToken createResetToken(User user) {
        var optionalToken = passwordResetRepository.findByUserId(user.getId());
        if (optionalToken.isPresent()) {
            PasswordResetToken existingToken = optionalToken.get();
            if (existingToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                existingToken.setToken(UUID.randomUUID().toString());
                existingToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
                return passwordResetRepository.save(existingToken);
            } else {
                return existingToken;
            }
        } else {
            PasswordResetToken newToken = new PasswordResetToken();
            newToken.setToken(UUID.randomUUID().toString());
            newToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
            newToken.setUser(user);
            return passwordResetRepository.save(newToken);
        }
    }

    public String sendResetPasswordLink(String email) {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        PasswordResetToken token = createResetToken(user);
        emailService.sendPasswordResetEmail(email, token.getToken());
        return "Password link has been sent to the user's email: " + email;
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest dto) {
        User user =
                userRepository
                        .findByEmail(dto.email())
                        .orElseThrow(() -> new ResourceNotFoundException("user is not found"));
        PasswordResetToken existedToken =
                passwordResetRepository
                        .findByUserId(user.getId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException("The token doesn't exist"));
        ;
        if (!dto.resetPasswordcode().equals(existedToken.getToken())) {
            throw new TokenMismatchException("Token mismatch error");
        }
        if (existedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Reset password token is expired");
        }
        if (passwordEncoder.matches(dto.newPassword(), user.getPassword())) {
            throw new SamePasswordException("new password must be different from old password.");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));

        passwordResetRepository.delete(existedToken);
        userRepository.save(user);
    }
}
