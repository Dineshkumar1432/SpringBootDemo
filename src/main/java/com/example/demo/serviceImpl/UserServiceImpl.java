package com.example.demo.serviceImpl;

import java.util.List;
// import java.util.NoSuchElementException;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    // GET all users (no pagination)
    @Cacheable(value = "usersWithoutPagination")

    public List<UserDTO> getUsersWithoutPagination() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(user.getId());
                    dto.setName(user.getName());
                    dto.setUsername(user.getUsername());
                    dto.setRole(user.getRole());
                    dto.setOrders(user.getOrders() != null
                            ? user.getOrders().stream()
                                    .map(o -> new OrderDTO(o.getProduct()))
                                    .toList()
                            : List.of());
                    return dto;
                })
                .toList();
    }

    // GET users with pagination
    @Cacheable(value = "usersList", key = "#page + '-' + #size")

    public Page<UserDTO> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(user -> {
            UserDTO dto = new UserDTO();
            dto.setName(user.getName());
            dto.setUsername(user.getUsername());
            dto.setId(user.getId());
            return dto;
        });
    }

    // ADD user
@CacheEvict(value = "usersList", allEntries = true)
public void addUser(User user) {

    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        throw new UserAlreadyExistsException("Username already exists");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword())); //  only here

    userRepository.save(user);
}

    // DELETE user

    @Caching(evict = {
            @CacheEvict(value = "userById", key = "#id"),
            @CacheEvict(value = "usersList", allEntries = true)
    })

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
    @CacheEvict(value = "usersList", allEntries = true)
    @CachePut(value = "usersById", key = "#id")
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

    // GET USER DTO
    @Transactional // VERY IMPORTANT
    @Cacheable(value = "usersById", key = "#id")
    public UserDTO getUser(int id) {

        System.out.println("Service DB CALL ");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());

        // SAFE: avoid lazy crash
        if (user.getOrders() != null) {
            List<OrderDTO> orders = user.getOrders()
                    .stream()
                    .map(o -> new OrderDTO(o.getProduct()))
                    .toList();
            // dto.setOrders(List.of()); // if you want to return empty list instead of
            // orders
            dto.setOrders(orders);
        } else {
            dto.setOrders(List.of());
        }

        return dto;
    }

    

}