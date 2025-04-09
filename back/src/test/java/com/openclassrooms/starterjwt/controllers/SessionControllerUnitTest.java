package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SessionControllerUnitTest {
  @InjectMocks
  private SessionController sessionController;

  @Mock
  private SessionMapper sessionMapper;

  @Mock
  private SessionService sessionService;

  private Session sessionId1;
  private Session sessionId2;
  private SessionDto sessionDto1;
  private SessionDto sessionDto2;

  @Mock
  private Teacher teacher1, teacher2;

  @Mock
  private User user1, user2, user3, user4;

  @BeforeEach
  void setUp() {
    sessionId1 = Session.builder()
            .id(1L)
            .name("Session 1")
            .date(new Date())
            .description("Description 1")
            .teacher(teacher1)
            .users(Arrays.asList(user1, user2))
            .build();

    sessionId2 = Session.builder()
            .id(2L)
            .name("Session 2")
            .date(new Date())
            .description("Description 2")
            .teacher(teacher2)
            .users(Arrays.asList(user3, user4))
            .build();

    sessionDto1 = new SessionDto();
    sessionDto1.setId(1L);
    sessionDto1.setName("Session 1");
    sessionDto1.setDate(new Date());
    sessionDto1.setDescription("Description 1");
    sessionDto1.setTeacher_id(1L);
    sessionDto1.setUsers(Arrays.asList(1L, 2L));

    sessionDto2 = new SessionDto();
    sessionDto2.setId(2L);
    sessionDto2.setName("Session 2");
    sessionDto2.setDate(new Date());
    sessionDto2.setDescription("Description 2");
    sessionDto2.setTeacher_id(2L);
    sessionDto2.setUsers(Arrays.asList(3L, 4L));
  }

  @Test
  @DisplayName("Get session by id successfully")
  public void getSessionByIdSuccessfully() {
    // Arrange
    when(sessionService.getById(1L)).thenReturn(sessionId1);
    when(sessionMapper.toDto(sessionId1)).thenReturn(sessionDto1);

    // Act
    ResponseEntity<?> response = sessionController.findById("1");

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(SessionDto.class, response.getBody().getClass());
    assertEquals(sessionDto1, response.getBody());
    verify(sessionService, times(1)).getById(1L);
    verify(sessionMapper, times(1)).toDto(sessionId1);
  }

  @Test
  @DisplayName("Get session by id with invalid id")
  public void getSessionByIdWithInvalidId() {
    // Arrange
    when(sessionService.getById(1L)).thenReturn(null);

    // Act
    ResponseEntity<?> response = sessionController.findById("1");

    // Assert
    assertEquals(404, response.getStatusCodeValue());
    verify(sessionService, times(1)).getById(1L);
  }

  @Test
  @DisplayName("Get session by id with invalid format")
  public void getSessionByIdWithInvalidFormat() {
    // Act
    ResponseEntity<?> response = sessionController.findById("invalid");

    // Assert
    assertEquals(400, response.getStatusCodeValue());
    verify(sessionService, times(0)).getById(anyLong());
  }

  @Test
  @DisplayName("Get all sessions successfully")
  public void getAllSessionsSuccessfully() {
    // Arrange
    when(sessionService.findAll()).thenReturn(Arrays.asList(sessionId1, sessionId2));
    when(sessionMapper.toDto(Arrays.asList(sessionId1, sessionId2))).thenReturn(Arrays.asList(sessionDto1, sessionDto2));

    // Act
    ResponseEntity<?> response = sessionController.findAll();

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(Arrays.asList(sessionDto1, sessionDto2), response.getBody());
    verify(sessionService, times(1)).findAll();
    verify(sessionMapper, times(1)).toDto(Arrays.asList(sessionId1, sessionId2));
  }

  @Test
  @DisplayName("Create session successfully")
  public void createSessionSuccessfully() {
    // Arrange
    when(sessionMapper.toEntity(sessionDto1)).thenReturn(sessionId1);
    when(sessionService.create(sessionId1)).thenReturn(sessionId1);
    when(sessionMapper.toDto(sessionId1)).thenReturn(sessionDto1);

    // Act
    ResponseEntity<?> response = sessionController.create(sessionDto1);

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(SessionDto.class, response.getBody().getClass());
    assertEquals(sessionDto1, response.getBody());
    verify(sessionMapper, times(1)).toEntity(sessionDto1);
    verify(sessionService, times(1)).create(sessionId1);
    verify(sessionMapper, times(1)).toDto(sessionId1);
  }

  @Test
  @DisplayName("Update session successfully")
  public void updateSessionSuccessfully() {
    // Arrange
    when(sessionService.update(1L, sessionId1)).thenReturn(sessionId1);
    when(sessionMapper.toEntity(sessionDto1)).thenReturn(sessionId1);
    when(sessionMapper.toDto(sessionId1)).thenReturn(sessionDto1);

    // Act
    ResponseEntity<?> response = sessionController.update("1", sessionDto1);

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(SessionDto.class, response.getBody().getClass());
    assertEquals(sessionDto1, response.getBody());
    verify(sessionService, times(1)).update(1L, sessionId1);
    verify(sessionMapper, times(1)).toEntity(sessionDto1);
    verify(sessionMapper, times(1)).toDto(sessionId1);
  }

  @Test
  @DisplayName("Update session with invalid format")
  public void updateSessionWithInvalidFormat() {
    // Act
    ResponseEntity<?> response = sessionController.update("invalid", sessionDto1);

    // Assert
    assertEquals(400, response.getStatusCodeValue());
    verify(sessionService, times(0)).update(anyLong(), any(Session.class));
  }

  @Test
  @DisplayName("Delete session successfully")
  public void deleteSessionSuccessfully() {
    // Arrange
    when(sessionService.getById(1L)).thenReturn(sessionId1);

    // Act
    ResponseEntity<?> response = sessionController.save("1");

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    verify(sessionService, times(1)).getById(1L);
    verify(sessionService, times(1)).delete(1L);
  }

  @Test
  @DisplayName("Delete session with invalid id")
  public void deleteSessionWithInvalidId() {
    // Arrange
    when(sessionService.getById(1L)).thenReturn(null);

    // Act
    ResponseEntity<?> response = sessionController.save("1");

    // Assert
    assertEquals(404, response.getStatusCodeValue());
    verify(sessionService, times(1)).getById(1L);
  }

  @Test
  @DisplayName("Delete session with invalid format")
  public void deleteSessionWithInvalidFormat() {
    // Act
    ResponseEntity<?> response = sessionController.save("invalid");

    // Assert
    assertEquals(400, response.getStatusCodeValue());
    verify(sessionService, times(0)).getById(anyLong());
  }

  @Test
  @DisplayName("Participate in session successfully")
  public void participateInSessionSuccessfully() {
    // Arrange

    // Act
    ResponseEntity<?> response = sessionController.participate("1", "2");

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    verify(sessionService, times(1)).participate(1L, 2L);
  }

  @Test
  @DisplayName("Participate in session with invalid id")
  public void participateInSessionWithInvalidId() {
    // Arrange

    // Act
    ResponseEntity<?> response = sessionController.participate("1", "invalid");

    // Assert
    assertEquals(400, response.getStatusCodeValue());
    verify(sessionService, times(0)).participate(anyLong(), anyLong());
  }

  @Test
  @DisplayName("Participate in session with invalid format")
  public void participateInSessionWithInvalidFormat() {
    // Act
    ResponseEntity<?> response = sessionController.participate("invalid", "2");

    // Assert
    assertEquals(400, response.getStatusCodeValue());
    verify(sessionService, times(0)).participate(anyLong(), anyLong());
  }

  @Test
  @DisplayName("No longer participate in session successfully")
  public void noLongerParticipateInSessionSuccessfully() {
    // Arrange

    // Act
    ResponseEntity<?> response = sessionController.noLongerParticipate("1", "2");

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    verify(sessionService, times(1)).noLongerParticipate(1L, 2L);
  }

  @Test
  @DisplayName("No longer participate in session with invalid format")
  public void noLongerParticipateInSessionWithInvalidFormat() {
    // Act
    ResponseEntity<?> response = sessionController.noLongerParticipate("invalid", "2");

    // Assert
    assertEquals(400, response.getStatusCodeValue());
    verify(sessionService, times(0)).noLongerParticipate(anyLong(), anyLong());
  }
}