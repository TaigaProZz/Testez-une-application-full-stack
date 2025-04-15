package com.openclassrooms.starterjwt.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootTest
public class JwtUtilsUnitTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("Generate jwt token and validate it")
    public void testGenerateJwtTokenAndValidateJwtToken() {
        // Arrange
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(2L, "a@a.a", "a", "a", false, "a");
        // ACT
        String token = jwtUtils.generateJwtToken(new UsernamePasswordAuthenticationToken(userDetailsImpl, null));
        // Assert
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    @DisplayName("Test validateJwtToken signature is valid with wrong secret")
    public void testValidateJwtTokenSignatureException() {
        // Arrange
        String token = Jwts.builder()
                .setSubject(("a@a.a"))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 86400000))
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();
        // Assert
        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    @DisplayName("Validate jwt token and return expired exception")
    public void testValidateJwtTokenExpiredJwtException() {
        // Arrange
        String token = Jwts.builder()
                .setSubject(("a@a.a"))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() - 1))
                .signWith(SignatureAlgorithm.HS512, "openclassrooms")
                .compact();
        // Assert
        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    @DisplayName("validate jwt token and return malformed jwt exception")
    public void testValidateJwtTokenMalformedJwtException() {
        // Assert
        assertFalse(jwtUtils.validateJwtToken("wrongToken"));
    }

    @Test
    @DisplayName("Test validateJwtToken and return IllegalArgumentException")
    public void testValidateJwtTokenIllegalArgumentException() {
        // Assert
        assertFalse(jwtUtils.validateJwtToken(""));
    }
}