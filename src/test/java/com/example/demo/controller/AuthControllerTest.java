package com.example.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.dto.LoginDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private UserRepository userRepository;

    @Test
    void testLogin() throws Exception {

        // create request object
        LoginDTO dto = new LoginDTO();

        dto.setUsername("dinesh");
        dto.setPassword("1234");

        // mock service response
        when(authService.login("dinesh", "1234"))
                .thenReturn("Login Successful");

        // perform POST request
        mockMvc.perform(post("/api/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))

                // verify status
                .andExpect(status().isOk())

                // verify response body
                .andExpect(content()
                        .string("Login Successful"));
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("dinesh");
        dto.setPassword("wrongPassword");

        // Mock the service to throw an exception for invalid credentials
        when(authService.login("dinesh", "wrongPassword"))
                .thenThrow(new RuntimeException("Invalid password"));

        // Expect the request to fail (400 or 401 depending on your error handling)
        mockMvc.perform(post("/api/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError()); // or .isBadRequest() / .isUnauthorized()
    }

    @Test
    void testRegister() {

    }
}
