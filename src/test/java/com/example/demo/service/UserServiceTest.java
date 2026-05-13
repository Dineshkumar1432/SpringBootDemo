package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testAddUser() {
        // create dummy user
        User user = new User();
        user.setUsername("dinesh");
        user.setPassword("1234");

        // repository returns empty
        when(userRepository.findByUsername("dinesh"))
                .thenReturn(Optional.empty());

        // mock encoded password
        when(passwordEncoder.encode("1234"))
                .thenReturn("encoded1234");

        // call service method
        userService.addUser(user);

        // verify save method called
        verify(userRepository).save(user);

        // verify password changed
        assertEquals("encoded1234",
                user.getPassword());
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(1))
                .thenReturn(true);

        // call service method
        userService.deleteUser(1);

        // verify deleteById called
        verify(userRepository).deleteById(1);
    }

    @Test
    void testGetUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("dinesh");

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        User result = userService.getUserById(1);

        assertEquals("dinesh", result.getUsername());
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1);
        user.setUsername("dinesh");

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        User result = userService.getUserById(1);

        assertEquals("dinesh", result.getUsername());
    }

    @Test
    void testGetUsers() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("dinesh");

        userRepository.save(user1);

        List<User> users = List.of(user1);

        when(userRepository.findAll())
                .thenReturn(users);

        List<User> result = userService.getUsersWithoutPagination();

        assertEquals(1, result.size());
        assertEquals("dinesh", result.get(0).getUsername());
    }

    @Test
    void testGetUsersWithoutPagination() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("dinesh");

        userRepository.save(user1);

        List<User> users = List.of(user1);

        when(userRepository.findAll())
                .thenReturn(users);

        List<User> result = userService.getUsersWithoutPagination();

        assertEquals(1, result.size());
        assertEquals("dinesh", result.get(0).getUsername());
    }

    @Test
    void testSearchByName() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("dinesh");

        userRepository.save(user1);

        List<User> users = List.of(user1);

        when(userRepository.findByNameContaining("dinesh"))
                .thenReturn(users);

        List<User> result = userService.searchByName("dinesh");

        assertEquals(1, result.size());
        assertEquals("dinesh", result.get(0).getUsername());
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("dinesh");
        user.setPassword("1234");

        User updatedDetails = new User();
        updatedDetails.setUsername("dinesh123");
        updatedDetails.setPassword("5678");

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.encode("5678"))
                .thenReturn("encoded5678");

        when(userRepository.save(user))
                .thenReturn(user);

        User result = userService.updateUser(1, updatedDetails);

        assertEquals("dinesh123", result.getUsername());
        assertEquals("encoded5678", result.getPassword());
    }

    // additional test cases for learning purposes
    @Test
    public void testAddition() {

        int result = 2 + 3;

        assertEquals(5, result);
    }
}
