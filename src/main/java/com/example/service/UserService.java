package com.example.service;

import com.example.exceptions.IllegalRequestException;
import com.example.exceptions.NotFoundException;
import com.example.model.entity.User;
import com.example.model.dto.SegmentDto;
import com.example.model.dto.UserDto;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    public List<SegmentDto> getUsersSegments(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new NotFoundException("user with id = " + id + " not found");

        return user.get().getSegments()
                .stream()
                .map(segment -> modelMapper.map(segment, SegmentDto.class))
                .toList();
    }

    public UserDto addUser(String email, String name, String surname) {
        boolean exists = userRepository.existsByEmail(email);
        if (exists) throw new IllegalRequestException("user with email = " + email + " already exists");
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setSurname(surname);

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new NotFoundException("user with id = " + id + " not found");

        userRepository.deleteById(id);
    }
}
