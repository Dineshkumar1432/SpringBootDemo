package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<User> getUsersWithoutPagination() {
        return userService.getUsersWithoutPagination();
    }

    @GetMapping("/usersPaginated")
    public Page<User> getUsers(
            @RequestParam int page,
            @RequestParam int size) {

        return userService.getUsers(page, size);
    }

    @GetMapping("/users/{id}")
    public UserDTO getUser(@PathVariable int id) {

        String loggedInUser = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        UserDTO user = userService.getUser(id);

        if (!user.getUsername().equals(loggedInUser) && !isAdmin) {
            throw new RuntimeException("Access Denied");
        }

        return user;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public String addUser(@Valid @RequestBody User user) {
        userService.addUser(user);
        return "User added successfully";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{id}")
    public void updateUser(@PathVariable int id, @RequestBody User user) {

        String loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserDTO existingUser = userService.getUser(id);

        // 🔥 Only owner can update
        if (!existingUser.getUsername().equals(loggedInUser)) {
            throw new RuntimeException("Access Denied");
        }

        userService.updateUser(id, user);
    }

    @GetMapping("/users/search")
    public List<User> searchUser(@RequestParam String name) {
        return userService.searchByName(name);
    }
}
