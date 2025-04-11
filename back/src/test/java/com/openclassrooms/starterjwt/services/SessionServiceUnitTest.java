package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class SessionServiceUnitTest {
  @InjectMocks
  private SessionService sessionService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private SessionRepository sessionRepository;

  private User user;
  private Session session;
  private List<Session> sessionsList;

  @BeforeEach
  public void setUp() {
    Teacher teacher = new Teacher();
    teacher.setId(1L);

    this.session = Session.builder()
            .id(1L)
            .name("Session 1")
            .date(new Date())
            .description("Description 1")
            .teacher(teacher)
            .users(new ArrayList<>())
            .build();



    this.sessionsList = new ArrayList<>();
    this.sessionsList.add(session);

    this.user = new User();
    user = new User();
    user.setId(1L);
    user.setEmail("a@a.a");
    user.setLastName("a");
    user.setFirstName("a");
    user.setPassword("a");
    user.setAdmin(false);
    user.setCreatedAt(null);
    user.setUpdatedAt(null);
  }

  @Test
  @DisplayName("Create session successfully")
  public void testCreateSession() {
    // Arrange
    when(sessionRepository.save(any(Session.class))).thenReturn(session);

    // Act
    Session createdSession = sessionService.create(session);

    // Assert
    assertNotNull(createdSession);
    assertEquals(session.getId(), createdSession.getId());
    assertEquals(session.getName(), createdSession.getName());
    verify(sessionRepository).save(session);
  }

  @Test
  @DisplayName("Delete session by id successfully")
  public void testDeleteSession() {
    // Arrange
    Long sessionId = 1L;

    // Act
    sessionService.delete(sessionId);

    // Assert
    verify(sessionRepository).deleteById(sessionId);
  }

  @Test
  @DisplayName("Find all sessions successfully")
  public void testFindAllSessions() {
    // Arrange
    when(sessionRepository.findAll()).thenReturn(sessionsList);

    // Act
    List<Session> foundSessions = sessionService.findAll();

    // Assert
    assertNotNull(foundSessions);
    assertEquals(1, foundSessions.size());
    assertEquals(session.getId(), foundSessions.get(0).getId());
    assertEquals(session.getName(), foundSessions.get(0).getName());
    verify(sessionRepository).findAll();
  }

  @Test
  @DisplayName("Get session by id successfully")
  public void testGetSessionById() {
    // Arrange
    Long sessionId = 1L;
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

    // Act
    Session foundSession = sessionService.getById(sessionId);

    // Assert
    assertNotNull(foundSession);
    assertEquals(session.getId(), foundSession.getId());
    assertEquals(session.getName(), foundSession.getName());
    verify(sessionRepository).findById(sessionId);
  }

  @Test
  @DisplayName("Get session by id with non-existing id")
  public void testGetSessionByIdWithNonExistingId() {
    // Arrange
    Long sessionId = 1L;
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

    // Act
    Session foundSession = sessionService.getById(sessionId);

    // Assert
    assertNull(foundSession);
    verify(sessionRepository).findById(sessionId);
  }

  @Test
  @DisplayName("Update session successfully")
  public void testUpdateSession() {
    // Arrange
    Long sessionId = 1L;
    when(sessionRepository.save(any(Session.class))).thenReturn(session);

    // Act
    Session updatedSession = sessionService.update(sessionId, session);

    // Assert
    assertNotNull(updatedSession);
    assertEquals(session.getId(), updatedSession.getId());
    assertEquals(session.getName(), updatedSession.getName());
    verify(sessionRepository).save(session);
  }

  @Test
  @DisplayName("Participate in session successfully")
  public void testParticipateInSession() {
    // Arrange
    Long sessionId = 1L;
    Long userId = 1L;
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(sessionRepository.save(session)).thenReturn(session);

    // Act
    sessionService.participate(sessionId, userId);

    // Assert
    assertEquals(1, session.getUsers().size());
    assertEquals(userId, session.getUsers().get(0).getId());
    verify(sessionRepository).save(session);
  }

  @Test
  @DisplayName("Participate in session but session not found")
  public void testParticipateInSessionButSessionNotFound() {
    // Arrange
    Long sessionId = 1L;
    Long userId = 1L;
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
    verify(sessionRepository).findById(sessionId);
  }

  @Test
  @DisplayName("Participate in session but user not found")
  public void testParticipateInSessionButUserNotFound() {
    // Arrange
    Long sessionId = 1L;
    Long userId = 1L;
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
    verify(userRepository).findById(userId);
  }

  @Test
  @DisplayName("Participate in session but already participating")
  public void testParticipateInSessionButAlreadyParticipating() {
    // Arrange
    Long sessionId = 1L;
    Long userId = 1L;
    session.getUsers().add(user);
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    // Act & Assert
    assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
    verify(sessionRepository).findById(sessionId);
  }

  @Test
  @DisplayName("No longer participate in session successfully")
  public void testNoLongerParticipateInSession() {
    // Arrange
    Long sessionId = 1L;
    Long userId = 1L;
    session.getUsers().add(user);
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

    // Act
    sessionService.noLongerParticipate(sessionId, userId);

    // Assert
    assertEquals(0, session.getUsers().size());
    verify(sessionRepository).save(session);
  }

  @Test
  @DisplayName("No longer participate in session but session not found")
  public void testNoLongerParticipateInSessionButSessionNotFound() {
    // Arrange
    Long sessionId = 1L;
    Long userId = 1L;
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
    verify(sessionRepository).findById(sessionId);
  }

  @Test
  @DisplayName("No longer participate in session but already not participating")
  public void testNoLongerParticipateInSessionButAlreadyNotParticipating() {
    // Arrange
    Long sessionId = 1L;
    Long userId = 1L;
    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

    // Act & Assert
    assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
    verify(sessionRepository).findById(sessionId);
  }



}