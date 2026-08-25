package com.parking_reservation_system.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.parking_reservation_system.model.User;
import com.parking_reservation_system.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityTestUtils {
    private SecurityTestUtils() {}

    public static void mockSecurityContext(User user) {
        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        when(customUserDetails.getUser()).thenReturn(user);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    public static void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
