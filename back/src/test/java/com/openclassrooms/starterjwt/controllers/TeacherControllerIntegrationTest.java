package com.openclassrooms.starterjwt.controllers;


import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class TeacherControllerIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

  String token;

  @BeforeAll
  public void getValidToken() throws Exception {
    String requestBody = "{\"email\": \"admin@admin.com\", \"password\": \"a\"}";

    MvcResult resultLogin = mockMvc.perform(post("/api/auth/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestBody))
      .andReturn();

    token = "Bearer " + JsonPath.read(resultLogin.getResponse().getContentAsString(), "$.token");
  }

  @Test
  @DisplayName("Get teacher by id")
  public void testGetTeacherById() throws Exception {
    String teacherId = "1";

    mockMvc.perform(get("/api/teacher/" + teacherId)
        .header("Authorization", token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(teacherId));
  }

  @Test
  @DisplayName("Get teacher by id with invalid id")
  public void testGetTeacherByIdWithInvalidId() throws Exception {
    String teacherId = "invalid-id";

    mockMvc.perform(get("/api/teacher/" + teacherId)
        .header("Authorization", token))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Get teacher by id with non-existing id")
  public void testGetTeacherByIdWithNonExistingId() throws Exception {
    String teacherId = "20";

    mockMvc.perform(get("/api/teacher/" + teacherId)
        .header("Authorization", token))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Get all teachers")
  public void testGetAllTeachers() throws Exception {
    mockMvc.perform(get("/api/teacher")
        .header("Authorization", token))
        .andExpect(status().isOk());
  }
}