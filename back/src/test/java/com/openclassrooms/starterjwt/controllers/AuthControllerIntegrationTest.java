package com.openclassrooms.starterjwt.controllers;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Test login as user with valid credentials")
  public void testLoginWithValidCredentials() throws Exception {
    String requestBody = "{\"email\": \"a@a.a\", \"password\": \"a\"}";

    MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andReturn();

    // Check if the response contains the expected fields
    assertTrue(mvcResult.getResponse().getContentAsString().contains("token"));
    assertTrue(mvcResult.getResponse().getContentAsString().contains("\"username\":\"a@a.a\""));
    assertTrue(mvcResult.getResponse().getContentAsString().contains("\"firstName\":\"a\""));
    assertTrue(mvcResult.getResponse().getContentAsString().contains("\"lastName\":\"a\""));
    assertTrue(mvcResult.getResponse().getContentAsString().contains("\"admin\":false"));
  }

  @Test
  @DisplayName("Test login as admin with valid credentials")
  public void testLoginWithValidAdminCredentials() throws Exception {
    String requestBody = "{\"email\": \"admin@admin.com\", \"password\": \"a\"}";

    MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andReturn();

    // Check if the response contains the expected fields
    assertTrue(mvcResult.getResponse().getContentAsString().contains("token"));
    assertTrue(mvcResult.getResponse().getContentAsString().contains("\"username\":\"admin@admin.com\""));
    assertTrue(mvcResult.getResponse().getContentAsString().contains("\"firstName\":\"admin\""));
    assertTrue(mvcResult.getResponse().getContentAsString().contains("\"lastName\":\"admin\""));
    assertTrue(mvcResult.getResponse().getContentAsString().contains("\"admin\":true"));
  }

  @Test
  @DisplayName("Test login with invalid credentials")
  public void testLoginWithInvalidCredentials() throws Exception {
    String requestBody = "{\"email\": \"invalid@a.a\", \"password\": \"wrongpassword\"}";

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Test register with valid credentials")
  public void testRegisterWithValidCredentials() throws Exception {
    String requestBody = "{\"email\": \"newuser@a.a\", \"password\": \"newpassword\", \"firstName\": \"New\", \"lastName\": \"User\"}";

    MvcResult mvcResult = mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andReturn();

    // Check if the response contains the expected fields
    assertTrue(mvcResult.getResponse().getContentAsString().contains("User registered successfully!"));
  }

  @Test
  @DisplayName("Test register with existing email")
  public void testRegisterWithExistingEmail() throws Exception {
    String requestBody = "{\"email\": \"a@a.a\", \"password\": \"newpassword\", \"firstName\": \"New\", \"lastName\": \"User\"}";

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(result -> {
                String responseBody = result.getResponse().getContentAsString();
                assertTrue(responseBody.contains("Error: Email is already taken!"));
            });
  }
}