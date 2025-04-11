package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {
  @InjectMocks
  private UserController userController;

  @Mock
  private SecurityContext securityContext;

  @Mock
  private UserMapper userMapper;

  @Mock
  private UserService userService;

  private User user;
  private UserDto userDto;

  private final Long id = 1L;
  private final String email = "a@a.a";
  private final String password = "a";
  private final String firstName = "a";
  private final String lastName = "a";
  private final Boolean isAdmin = true;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(id);
    user.setEmail(email);
    user.setLastName(lastName);
    user.setFirstName(firstName);
    user.setPassword(password);
    user.setAdmin(isAdmin);
    user.setCreatedAt(null);
    user.setUpdatedAt(null);

    userDto = new UserDto();
    userDto.setId(id);
    userDto.setEmail(email);
    userDto.setLastName(lastName);
    userDto.setFirstName(firstName);
    userDto.setPassword(password);
    userDto.setAdmin(isAdmin);
  }

  @Test
  @DisplayName("Find an user by ID successfully")
  public void findUserBydIdSuccessfully() {
    // Arrange
    when(this.userService.findById(id)).thenReturn(user);
    when(this.userMapper.toDto(user)).thenReturn(userDto);

    // Act
    ResponseEntity<?> response = this.userController.findById(String.valueOf(id));

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userDto, response.getBody());
  }

  @Test
  @DisplayName("Find an user by ID with bad ID")
  public void findUserBydIdWithBadId() {
    // Arrange
    String badId = "badId";

    // Act
    ResponseEntity<?> response = this.userController.findById(badId);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  @DisplayName("Find an user by ID not found")
  public void findUserBydIdNotFound() {
    // Arrange
    when(this.userService.findById(id)).thenReturn(null);

    // Act
    ResponseEntity<?> response = this.userController.findById(String.valueOf(id));

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  @DisplayName("Delete an user by ID successfully")
  public void deleteUserBydIdSuccessfully() {
    // Arrange
    UserDetailsImpl userDetailsImpl = new UserDetailsImpl(id, email, firstName,
            lastName, isAdmin, password);
    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsImpl, null);
    SecurityContextHolder.setContext(securityContext);

    when(this.userService.findById(id)).thenReturn(user);
    when(securityContext.getAuthentication()).thenReturn(authentication);

    // Act
    ResponseEntity<?> response = this.userController.save(String.valueOf(id));

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  @DisplayName("Delete an user by ID with bad ID")
  public void deleteUserBydIdWithBadId() {
    // Arrange
    String badId = "badId";

    // Act
    ResponseEntity<?> response = this.userController.save(badId);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  @DisplayName("Delete an user by ID not found")
  public void deleteUserBydIdNotFound() {
    // Arrange
    when(this.userService.findById(id)).thenReturn(null);

    // Act
    ResponseEntity<?> response = this.userController.save(String.valueOf(id));

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  @DisplayName("Delete an user by ID not authorized")
  public void deleteUserBydIdNotAuthorized() {
    // Arrange
    when(this.userService.findById(id)).thenReturn(user);

    UserDetailsImpl userDetailsImpl = new UserDetailsImpl(id, "b@b.b", firstName,
            lastName, isAdmin, password);
    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsImpl, null);
    SecurityContextHolder.setContext(securityContext);

    when(securityContext.getAuthentication()).thenReturn(authentication);

    // Act
    ResponseEntity<?> response = this.userController.save(String.valueOf(id));

    // Assert
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }
}