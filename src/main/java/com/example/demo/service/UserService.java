package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;

public interface UserService {

    List<UserDTO> getUsersWithoutPagination();

    Page<UserDTO> getUsers(int page, int size);

    void addUser(User user);

    void deleteUser(int id);

    User updateUser(int id, User userDetails);

    List<User> searchByName(String name);

    UserDTO getUser(int id);
}
