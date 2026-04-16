package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // public UserService(UserRepository userRepository) {
    // this.userRepository = userRepository;
    // }

    // public List<User> getUsers() {
    // return userRepository.findAll();
    // }

    public Page<User> getUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return userRepository.findAll(pageable);
    }

    public void addUser(User user) {

        userRepository.save(user);
    }

    // public void deleteUser(int id) {

    // if (!userRepository.existsById(id)) {
    // throw new NoSuchElementException("User not found with id: " + id);
    // }

    // userRepository.deleteById(id);
    // userRepository.save(user);
    // }

    public void deleteUser(int id) {

        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    public User updateUser(int id, User userDetails) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        user.setName(userDetails.getName());

        return userRepository.save(user);
    }

    public List<User> searchByName(String name) {
        return userRepository.findByNameContaining(name);
    }

    public UserDTO getUser(int id) {

        User user = userRepository.findById(id).orElseThrow();

        UserDTO dto = new UserDTO(

        );
        List<OrderDTO> orderDTOs = user.getOrders()
                .stream()
                .map(order -> new OrderDTO(order.getProduct()))
                .toList();
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setOrders(orderDTOs);

        return dto;
    }
}
