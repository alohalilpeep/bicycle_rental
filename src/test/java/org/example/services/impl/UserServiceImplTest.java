package org.example.services.impl;

import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.services.dto.UserDto;
import org.example.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto validUserDto;
    private UserDto invalidUserDto;
    private User user;

    @BeforeEach
    void setUp() {
        validUserDto = new UserDto();
        validUserDto.setEmail("test@example.com");


        invalidUserDto = new UserDto();
        invalidUserDto.setEmail("invalid-email");

        user = new User();
        user.setId(String.valueOf(1L));
        user.setEmail("test@example.com");
    }

    @Test
    void registerUserWithValidDtoShouldSaveUser() {
        // Arrange
        when(validationUtil.isValid(validUserDto)).thenReturn(true);
        when(userRepository.findByEmail(validUserDto.getEmail())).thenReturn(null);
        when(modelMapper.map(validUserDto, User.class)).thenReturn(user);

        // Act
        userService.registerUser(validUserDto);

        // Assert
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void registerUserWithInvalidDtoShouldNotSaveUser() {
        // Arrange
        when(validationUtil.isValid(invalidUserDto)).thenReturn(false);

        // Act
        userService.registerUser(invalidUserDto);

        // Assert
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUserWithExistingEmailShouldNotSaveUser() {
        // Arrange
        when(validationUtil.isValid(validUserDto)).thenReturn(true);
        when(userRepository.findByEmail(validUserDto.getEmail())).thenReturn(user);

        // Act
        userService.registerUser(validUserDto);

        // Assert
        verify(userRepository, never()).save(any());
    }

    @Test
    void findByEmailWithExistingEmailShouldReturnUser() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        User result = userService.findByEmail(email);

        // Assert
        assertEquals(user, result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByEmailWithNonExistingEmailShouldReturnNull() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act
        User result = userService.findByEmail(email);

        // Assert
        assertNull(result);
    }

    @Test
    void findAllActiveUsersShouldReturnActiveUsers() {
        // Arrange
        List<User> activeUsers = Collections.singletonList(user);
        when(userRepository.findByIsActiveTrue()).thenReturn(activeUsers);

        // Act
        List<User> result = userService.findAllActiveUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        verify(userRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    void findAllActiveUsersWhenNoActiveUsersShouldReturnEmptyList() {
        // Arrange
        when(userRepository.findByIsActiveTrue()).thenReturn(Collections.emptyList());

        // Act
        List<User> result = userService.findAllActiveUsers();

        // Assert
        assertTrue(result.isEmpty());
    }
}
