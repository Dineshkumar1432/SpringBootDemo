package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // GET all users (no pagination)
    public List<User> getUsersWithoutPagination() {
        return userRepository.findAll();
    }

    // GET users with pagination
    public Page<User> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    // ADD user
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // DELETE user
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // GET user by ID
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    // UPDATE user
    public User updateUser(int id, User userDetails) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        user.setName(userDetails.getName());
        user.setUsername(userDetails.getUsername());

        // encode password if updated
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    // SEARCH by name
    public List<User> searchByName(String name) {
        return userRepository.findByNameContaining(name);
    }

    // GET USER DTO (safe response)
    public UserDTO getUser(int id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

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