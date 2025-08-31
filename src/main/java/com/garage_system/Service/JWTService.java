package com.garage_system.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import com.garage_system.Model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class JWTService {

    @Value("${JWT_SECRET}")
    private String secretKey; // must be Base64 256 or 32Bit


    private final AuthenticationManager authManager;

    public JWTService(AuthenticationManager authManager) {
        this.authManager = authManager;
    }


    // Decode Base64 secret key into SecretKey object
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract username (subject) from token
    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract all claims (payload)
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                   .setSigningKey(getSigningKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // Check if token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate token against UserDetails
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Generate JWT token
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>(); // optional extra info

        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(username)
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 30)) // 30 hours
                   .signWith(getSigningKey()) // modern jjwt 0.12.x
                   .compact();
    }

    public ResponseEntity<?>verify(@RequestBody User user){
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        ); // delegates to your DaoAuthenticationProvider

        if (authentication.isAuthenticated()) {
            String token = this.generateToken(user.getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
