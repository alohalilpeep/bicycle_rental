package org.example.services;

import org.example.models.User;
import org.example.services.dto.UserDto;

import java.util.List;

public interface UserService {
    void registerUser(UserDto userDto);

    User findByEmail(String email);

    List<User> findAllActiveUsers();
}
