package com.parking_reserveration_system.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.parking_reservation_system.dto.request.LoginUserDto;
import com.parking_reservation_system.dto.request.RegisterUserDto;
import com.parking_reservation_system.service.AuthenticationService;

@DataJpaTest
public class AuthenticationServiceTest {

    @Autowired
    AuthenticationService service;


   /* @Test
   void RegisterUserServiceTest (){
      service.RegisterUser(new RegisterUserDto(1, "mido", "email@gmail.com", "123456789",
       "01009389599", ["USER"]));
    }*/

    @Test
    void loginUserTest(){
        testLoginWithWrongPassword();
      //  testLoginWithWrongEmail();
       // testLoginWithWrongPassword();
    }
    @Test
     void  resetPasswordTests(){
       // testResetPasswordGeneratesToken();
      //  testResetPasswordInvalidEmail();
       // testResetPasswordExpiredToken();
    }
    @Test
     void emailVerificationTests(){
      //  testVerifyEmailWithValidToken();
      //  testVerifyEmailWithExpiredToken();
       // testVerifyEmailAlreadyVerified();
    }


    public void testLoginWithWrongPassword(){
        service.loginUser(new LoginUserDto(1, "email@gmail.com", "123"));
    }
}
