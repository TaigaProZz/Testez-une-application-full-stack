package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
public class AuthControllerUnitTest {
  @InjectMocks
  private AuthController authController;

  @Mock
  private AuthenticationManager authenticationManager;
  @Mock
  private JwtUtils jwtUtils;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private UserRepository userRepository;

  private LoginRequest loginRequest;
  private SignupRequest signupRequest;
  private UserDetailsImpl userDetailsImpl;

  private final Long id = 1L;
  private final String email = "a@a.a";
  private final String password = "a";
  private final String firstName = "a";
  private final String lastName = "a";
  private final Boolean isAdmin = true;

  @BeforeEach
  void setUp() {
    loginRequest = new LoginRequest();
    loginRequest.setEmail(email);
    loginRequest.setPassword(password);

    signupRequest = new SignupRequest();
    signupRequest.setEmail(email);
    signupRequest.setPassword(password);
    signupRequest.setLastName(lastName);
    signupRequest.setFirstName(firstName);

    userDetailsImpl = UserDetailsImpl
            .builder()
            .username(email)
            .firstName(firstName)
            .lastName(lastName)
            .id(id)
            .password(password)
            .build();
  }

  @Test
  @DisplayName("Login user successfully")
  public void loginUserSuccessfully() {
    // Arrange
    String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiZW1haWwiOiJhQGEuYSIsInBhc3N3b3JkIjoiYSIsImZpcnN0bmFtZSI6ImEiLCJsYXN0bmFtZSI6ImEiLCJpc0FkbWluIjp0cnVlLCJpYXQiOjE3MTE3NjI3MDAsImV4cCI6MTcxMTc2NjMwMH0.c2lnbmF0dXJlLXNpbXVsZWUtYWN0dWVsbGUtbm9uLXZhbGlk";
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetailsImpl, null);

    when(authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())))
            .thenReturn(authentication);
    when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwt);
    when(userRepository.findByEmail(userDetailsImpl.getUsername()))
            .thenReturn(Optional.of(new User(id, email, lastName, firstName, password, isAdmin, null, null)));


    // Act
    ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);
    JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(jwt,  jwtResponse.getToken());
    assertEquals(id, jwtResponse.getId());
    assertEquals(email, jwtResponse.getUsername());
    assertEquals(firstName, jwtResponse.getFirstName());
    assertEquals(lastName, jwtResponse.getLastName());
    assertEquals(isAdmin, jwtResponse.getAdmin());
  }

  @Test
  @DisplayName("Register user successfully")
  public void testRegisterUserSuccessfully() {
    // Arrange
    when(userRepository.existsByEmail(email)).thenReturn(false);
    when(passwordEncoder.encode(password)).thenReturn("passwordEncode");
    when(userRepository.save(any(User.class))).thenReturn(new User());

    // Act
    ResponseEntity<?> response = authController.registerUser(signupRequest);
    MessageResponse messageResponse = (MessageResponse) response.getBody();

    // Assert
    assertEquals("User registered successfully!", messageResponse != null ? messageResponse.getMessage() : null);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  @DisplayName("Register but email already taken")
  public void testRegisterEmailAlreadyTaken() {
    // Arrange
    when(userRepository.existsByEmail(email)).thenReturn(true);

    // Act
    ResponseEntity<?> response = authController.registerUser(signupRequest);
    MessageResponse messageResponse = (MessageResponse) response.getBody();

    // Assert
    assertEquals("Error: Email is already taken!", messageResponse != null ? messageResponse.getMessage() : null);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

}