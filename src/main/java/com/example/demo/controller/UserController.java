package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @GetMapping("/users")
    // public List<User> getUsers() {
    // return userService.getUsers();
    // }
    @GetMapping("/users")
    public Page<User> getUsers(
            @RequestParam int page,
            @RequestParam int size) {

        return userService.getUsers(page, size);
    }

    @GetMapping("/users/{id}")
    public UserDTO getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PostMapping("/users")
    public String addUser(@Valid @RequestBody User user) {

        userService.addUser(user);
        return "User added successfully";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    @PutMapping("/users/{id}")
    public void updateUser(@PathVariable int id, @RequestBody User user) {
        userService.updateUser(id, user);
    }

    @GetMapping("/users/search")
    public List<User> searchUser(@RequestParam String name) {
        return userService.searchByName(name);
    }
}
