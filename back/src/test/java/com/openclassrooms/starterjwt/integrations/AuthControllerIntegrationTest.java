package com.openclassrooms.starterjwt.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:before-each.sql")
public class AuthControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;



  private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("Register User but email already taken")
  public void shouldNotRegisterUser_EmailAlreadyTaken() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setEmail("a@a.a");
    signupRequest.setFirstName("FirstName");
    signupRequest.setLastName("LastName");
    signupRequest.setPassword("Password");

    mockMvc.perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signupRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
  }

  @Test
  @DisplayName("Login User with incorrect Credentials")
  public void shouldNotAuthenticateUser_IncorrectCredentials() throws Exception {
    LoginRequest wrongLoginRequest = new LoginRequest();
    wrongLoginRequest.setEmail("a@a.a");
    wrongLoginRequest.setPassword("wrongpassword");

    mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(wrongLoginRequest)))
            .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Register then login User successfully")
  public void shouldRegisterThenLoginUser() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setEmail("newemail@gmail.com");
    signupRequest.setFirstName("FirstName");
    signupRequest.setLastName("LastName");
    signupRequest.setPassword("Password");


    String userJson = objectMapper.writeValueAsString(signupRequest);

    mockMvc.perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(userJson))
            .andExpect(status().isOk());

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail("newemail@gmail.com");
    loginRequest.setPassword("Password");

    String loginJson = objectMapper.writeValueAsString(loginRequest);

    mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty());
  }


}