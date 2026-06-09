package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginDTO;
import com.example.demo.model.User;
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

        @MockBean
        private AuthenticationManager authenticationManager;

        @Test
        void testLogin() throws Exception {

                LoginDTO dto = new LoginDTO();
                dto.setUsername("dinesh");
                dto.setPassword("1234");

                User user = new User();
                user.setId(1);
                user.setUsername("dinesh");
                user.setRole("USER");

                Authentication auth = mock(Authentication.class);

                when(authenticationManager.authenticate(any(Authentication.class)))
                                .thenReturn(auth);

                when(userRepository.findByUsername(anyString()))
                                .thenReturn(Optional.of(user));

                when(jwtUtil.generateToken(any(User.class)))
                                .thenReturn("test-token");

                mockMvc.perform(post("/auth/login")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").value("test-token"))
                                .andExpect(jsonPath("$.role").value("USER"))
                                .andExpect(jsonPath("$.userId").value(1));
        }

        @Test
        void testLoginWithInvalidCredentials() throws Exception {
                LoginDTO dto = new LoginDTO();
                dto.setUsername("dinesh");
                dto.setPassword("wrongPassword");

                when(authenticationManager.authenticate(any(Authentication.class)))
                                .thenThrow(new BadCredentialsException("Invalid password"));

                mockMvc.perform(post("/auth/login")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testRegister() {
                // no-op placeholder test
        }
}
