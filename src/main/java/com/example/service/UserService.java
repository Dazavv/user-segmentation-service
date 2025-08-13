package com.example.service;

import com.example.exceptions.IllegalRequestException;
import com.example.exceptions.NotFoundException;
import com.example.model.entity.Segment;
import com.example.model.entity.User;
import com.example.model.dto.SegmentDto;
import com.example.model.dto.UserDto;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    UserDto dto = modelMapper.map(user, UserDto.class);
                    dto.setSegmentsCode(
                            user.getSegments().stream()
                                    .map(Segment::getCode)
                                    .collect(Collectors.toSet())
                    );
                    return dto;
                })
                .toList();
    }

    public List<String> getUsersSegments(Long id) {
        User user = getUserById(id);

        return user.getSegments()
                .stream()
                .map(Segment::getCode)
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
        User user = getUserById(id);
        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new NotFoundException("User with id = " + id + " not found");
        return user.get();
    }

    public List<User> getExistedUsersById(Set<Long> usersId) {
        List<User> users = userRepository.findAllById(usersId);
        if (users.size() != usersId.size()) throw new NotFoundException("Not all users with given IDs = " + usersId + " was found");

        return users;
    }
}
