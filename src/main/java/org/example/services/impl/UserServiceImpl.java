package org.example.services.impl;

import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.services.UserService;
import org.example.services.dto.UserDto;
import org.example.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ValidationUtil validationUtil,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void registerUser(UserDto userDto) {
        if (!validationUtil.isValid(userDto)) {
            validationUtil.violations(userDto)
                    .forEach(v -> System.out.println(v.getMessage()));
            return;
        }

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            System.out.println("Email already exists");
            return;
        }

        User user = modelMapper.map(userDto, User.class);
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }
}