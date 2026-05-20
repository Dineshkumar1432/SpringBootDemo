package com.example.demo.service;

import java.util.List;
// import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // GET all users (no pagination)
    @Cacheable(value = "usersWithoutPagination")

    public List<User> getUsersWithoutPagination() {
        return userRepository.findAll();
    }

    // GET users with pagination
    @Cacheable(value = "users", key = "#page + '-' + #size")

    public Page<User> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    // ADD user

    public void addUser(User user) {
        if (userRepository.findByUsername(
                user.getUsername()).isPresent()) {

            throw new UserAlreadyExistsException(
                    "Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // DELETE user
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(
                    "User not found");
        }
        userRepository.deleteById(id);
    }

    // GET user by ID
    // public User getUserById(int id) {
    // return userRepository.findById(id)
    // .orElseThrow(() -> new UserNotFoundException(
    // "User not found"));
    // }

    // UPDATE user
    @CachePut(value = "users", key = "#id")
    public User updateUser(int id, User userDetails) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found"));

        user.setName(userDetails.getName());
        user.setUsername(userDetails.getUsername());

        // encode password if updated
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    // SEARCH by name
    @Cacheable(value = "users", key = "#name")

    public List<User> searchByName(String name) {
        return userRepository.findByNameContaining(name);
    }

    // GET USER DTO (safe response)
    @Cacheable(value = "users", key = "#id")
    public UserDTO getUser(int id) {

        System.out.println(" Service DB CALL");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserDTO dto = new UserDTO();

        dto.setName(user.getName());
        dto.setUsername(user.getUsername());

        List<OrderDTO> orderDTOs = (user.getOrders() == null)
                ? List.of()
                : user.getOrders()
                        .stream()
                        .map(order -> new OrderDTO(order.getProduct()))
                        .toList();

        dto.setOrders(orderDTOs);

        return dto;
    }
}