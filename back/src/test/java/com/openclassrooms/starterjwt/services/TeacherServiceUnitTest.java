package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TeacherServiceUnitTest {
  @InjectMocks
  private TeacherService teacherService;

  @Mock
  private TeacherRepository teacherRepository;

  private Teacher teacher;
  private Teacher teacher2;
  private List<Teacher> teachersList;

  @BeforeEach
  public void setUp() {
    this.teacher = new Teacher();
    teacher.setId(1L);

    this.teacher2 = new Teacher();
    teacher2.setId(2L);

    this.teachersList = new ArrayList<>();
    this.teachersList.add(teacher);
    this.teachersList.add(teacher2);
  }

  @Test
  @DisplayName("Find all teachers successfully")
  public void findAllTeachers() {
    // Arrange
    when(teacherService.findAll()).thenReturn(teachersList);

    // Act
    List<Teacher> result = teacherService.findAll();

    // Assert
    assertEquals(2, result.size());
    assertEquals(teacher, result.get(0));
    assertEquals(teacher2, result.get(1));
  }

  @Test
  @DisplayName("Find a teacher by ID successfully")
  public void findTeacherByIdSuccessfully() {
    // Arrange
    when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

    // Act
    Teacher result = teacherService.findById(1L);

    // Assert
    assertEquals(teacher, result);
    verify(teacherRepository).findById(1L);
  }

  @Test
  @DisplayName("Find a teacher by ID not found")
  public void findTeacherByIdNotFound() {
    // Arrange
    Long teacherId = 20L;
    when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

    // Act
    Teacher result = teacherService.findById(teacherId);

    // Assert
    assertNull(result);
  }
}