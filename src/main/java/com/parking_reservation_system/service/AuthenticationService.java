package com.parking_reservation_system.service;

import com.parking_reservation_system.dto.request.EmailVerificationDto;
import com.parking_reservation_system.dto.request.LoginUserDto;
import com.parking_reservation_system.dto.request.RegisterUserDto;
import com.parking_reservation_system.dto.request.ResetPasswordDto;
import com.parking_reservation_system.dto.response.EmailVerificationResponseDto;
import com.parking_reservation_system.dto.response.RegisterUserResponseDto;
import com.parking_reservation_system.exception.ResourceNotFoundException;
import com.parking_reservation_system.mapper.UserMapper;
import com.parking_reservation_system.model.PasswordResetToken;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.repository.PasswordResetRepository;
import com.parking_reservation_system.repository.UserRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

    private static SecureRandom RANDOM = new SecureRandom();

    ////TODO : we violate SRP here we are not only saving user to DB but we generate code , set expiration time
    
    /// TODO : function names i forgot and made it PASCAL it should be Camel
    public RegisterUserResponseDto RegisterUser(RegisterUserDto userDto) {
        /// TODO : bad naming it should be discritpitve for its intent
        boolean check = userRepository.existsByEmail(userDto.email());
        /// TODO :  Throwing Spring DB exception from Bussiness layer , throw a custom domain exception with clean HTTP 409 Conflict.
        if (check) { 
            throw new DataIntegrityViolationException("User already registered");
        }

        ////  TODO : this is not the best practice for generating a code it is vulnurable 
        String code = String.format("%05d", RANDOM.nextInt(100_000));
        User user = UserMapper.toUser(userDto, passwordEncoder);
        user.setVerificationCode(code);
        user.setVerified(false);
        user.setExpirationTime(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        //// Network calls (like emails) should happen outside database transactions investigate this , consider email exceptions inside registeration process.
        emailService.sendVerificationEmail(userDto.email(), code);

        return new RegisterUserResponseDto(
                user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRoles());
    }

    public EmailVerificationResponseDto verifyUser(EmailVerificationDto dto)
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
            throw new RuntimeException("Invalid verification code");
        }
        return new EmailVerificationResponseDto(dto.verificationCode(), dto.email());
    }

    public void resendVerificationCode(String email) throws RuntimeException {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        if (user.isVerified()) {
            throw new RuntimeException("User is already verified");
        }
        String code = String.format("%05d", RANDOM.nextInt(100_000));

        user.setVerificationCode(code);
        user.setExpirationTime(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);
        emailService.sendVerificationEmail(email, code);
    }

    public String loginUser(LoginUserDto userDto) {

        User registeredUser =
                userRepository
                        .findByEmail(userDto.email())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!registeredUser.isVerified())
            throw new RuntimeException("account has not verifiyed yet");

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.email(), userDto.password()));
        return jwtService.generateToken(userDto.email());
    }

    //// key generation and saving should be indeepndent from sening mail
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

    public String resetPassword(ResetPasswordDto dto, String Urltoken) {
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
        /// verifying
        if (!Urltoken.equals(existedToken.getToken())) {
            throw new RuntimeException("Token mismatch error");
        }
        /// is expired
        if (existedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset password token is expired");
        }
        /// same as old password
        if (passwordEncoder.matches(dto.newPassword(), user.getPassword())) {
            throw new RuntimeException("old password must be different from old password");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        /// consume after using so it wont be used infinitly
        passwordResetRepository.delete(existedToken);
        userRepository.save(user);

        return "password is reset successfully";
    }
}
