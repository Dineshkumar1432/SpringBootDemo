package com.example.demo.controller;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.example.demo.dto.UserDTO;
import com.example.demo.exception.UnauthorizedUserAccessException;
import com.example.demo.model.User;
import com.example.demo.serviceImpl.UserServiceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.example.demo.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    public UserController(UserServiceImpl userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository=userRepository;
    }

    // GET ALL USERS (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDTO> getUsersWithoutPagination() {
        return userService.getUsersWithoutPagination();
    }

    // PAGINATION
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginated")
    public Page<UserDTO> getUsers(
            @RequestParam int page,
            @RequestParam int size) {
        return userService.getUsers(page, size);
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable int id) {
        System.out.println("CONTROLLER METHOD ");

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
            throw new UnauthorizedUserAccessException("Access Denied");
        }

        return user;
    }

    // ADD USER
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String addUser(@Valid @RequestBody User user) {
        userService.addUser(user);
        return "User added successfully";
    }

    // DELETE USER
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @CacheEvict(value = "users", key = "#id")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    // UPDATE USER
    @PutMapping("/{id}")
    @CachePut(value = "users", key = "#id")
    public void updateUser(@PathVariable int id, @RequestBody User user) {

        String loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserDTO existingUser = userService.getUser(id);

        if (!existingUser.getUsername().equals(loggedInUser)) {
            throw new RuntimeException("Access Denied");
        }

        userService.updateUser(id, user);
    }

    // SEARCH USER
    @GetMapping("/search")
    public List<User> searchUser(@RequestParam String name) {
        return userService.searchByName(name);
    }

@GetMapping("/{id}/photo")
public ResponseEntity<byte[]> getUserPhoto(@PathVariable int id) {

    String loggedInUser = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName();

    boolean isAdmin = SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities()
            .stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

    User user = userRepository.findById(id).orElse(null);

    if (user == null) {
        return ResponseEntity.notFound().build();
    }

    // AUTH CHECK
    if (!user.getUsername().equals(loggedInUser) && !isAdmin) {
        throw new UnauthorizedUserAccessException("Access Denied");
    }

    // NO IMAGE → return empty 200
    if (user.getPhoto() == null) {
        return ResponseEntity.ok().build();   // IMPORTANT
    }

    return ResponseEntity.ok()
            .contentType(
                user.getPhotoType() != null
                        ? MediaType.valueOf(user.getPhotoType())
                        : MediaType.IMAGE_JPEG
            )
            .body(user.getPhoto());
}
}