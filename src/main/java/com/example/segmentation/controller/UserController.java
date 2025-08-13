package com.example.segmentation.controller;

import com.example.segmentation.model.dto.UserDto;
import com.example.segmentation.model.requests.CreateUserRequest;
import com.example.segmentation.service.UserService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "{id}")
    public List<String> getUsersSegments(@PathVariable @Min(1) Long id) {
        return userService.getUsersSegments(id);
    }

    @PostMapping
    public UserDto addUser(@RequestBody CreateUserRequest request) {
        return userService.addUser(request.getEmail(), request.getName(), request.getSurname());
    }

    @PutMapping(path = "{id}")
    public void deleteUser(@PathVariable @Min(1) Long id) {
        userService.deleteUser(id);
    }
}
