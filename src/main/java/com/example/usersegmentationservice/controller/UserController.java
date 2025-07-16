package com.example.usersegmentationservice.controller;

import com.example.usersegmentationservice.model.Segment;
import com.example.usersegmentationservice.model.User;
import com.example.usersegmentationservice.service.UserService;
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
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "{id}")
    public List<Segment> getUsersSegments(@PathVariable Long id) {
        return userService.getUsersSegments(id);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping(path = "{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
