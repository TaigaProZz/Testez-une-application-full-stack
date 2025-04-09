package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TeacherControllerUnitTest {
  @InjectMocks
  private TeacherController teacherController;

  @Mock
  private TeacherMapper teacherMapper;

  @Mock
  private TeacherService teacherService;

  private Teacher teacherId1;
  private Teacher teacherId2;
  private TeacherDto teacherDto1;
  private TeacherDto teacherDto2;

  @BeforeEach
  void setUp() {
    teacherId1 = new Teacher();
    teacherId1.setId(1L);

    teacherId2 = new Teacher();
    teacherId2.setId(2L);

    teacherDto1 = new TeacherDto();
    teacherDto1.setId(1L);

    teacherDto2 = new TeacherDto();
    teacherDto2.setId(2L);
  }

  @Test
  @DisplayName("Get teacher by id successfully")
  public void getSessionByIdSuccessfully() {
    // Arrange
    when(teacherService.findById(teacherId1.getId())).thenReturn(teacherId1);
    when(teacherMapper.toDto(teacherId1)).thenReturn(teacherDto1);

    // Act
    ResponseEntity<?> response = teacherController.findById("1");

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    assertInstanceOf(TeacherDto.class, response.getBody());
    verify(teacherService, times(1)).findById(teacherId1.getId());
    verify(teacherMapper, times(1)).toDto(teacherId1);
  }

  @Test
  @DisplayName("Get teacher by id with invalid id")
  public void getSessionByIdWithInvalidId() {
    // Arrange
    when(teacherService.findById(teacherId1.getId())).thenReturn(null);

    // Act
    ResponseEntity<?> response = teacherController.findById("1");

    // Assert
    assertEquals(404, response.getStatusCodeValue());
    verify(teacherService, times(1)).findById(teacherId1.getId());
  }

  @Test
  @DisplayName("Get teacher by id with invalid format")
  public void getSessionByIdWithInvalidFormat() {
    // Arrange
    String invalidId = "invalid";

    // Act
    ResponseEntity<?> response = teacherController.findById(invalidId);

    // Assert
    assertEquals(400, response.getStatusCodeValue());
    verify(teacherService, never()).findById(anyLong());
  }

  @Test
  @DisplayName("Get all teachers successfully")
  public void getAllTeachersSuccessfully() {
    // Arrange
    when(teacherService.findAll()).thenReturn(Arrays.asList(teacherId1, teacherId2));
    when(teacherMapper.toDto(Arrays.asList(teacherId1, teacherId2))).thenReturn(Arrays.asList(teacherDto1, teacherDto2));

    // Act
    ResponseEntity<?> response = teacherController.findAll();

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    assertInstanceOf(List.class, response.getBody());
    verify(teacherService, times(1)).findAll();
    verify(teacherMapper, times(1)).toDto(Arrays.asList(teacherId1, teacherId2));
  }
}