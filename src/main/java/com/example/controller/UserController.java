package com.example.controller;

import com.example.model.dto.SegmentDto;
import com.example.model.dto.UserDto;
import com.example.model.requests.CreateUserRequest;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "{id}")
    public List<SegmentDto> getUsersSegments(@PathVariable Long id) {
        return userService.getUsersSegments(id);
    }

    @PostMapping
    public UserDto addUser(@RequestBody CreateUserRequest request) {
        return userService.addUser(request.getEmail(), request.getName(), request.getSurname());
    }

    @PutMapping(path = "{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
