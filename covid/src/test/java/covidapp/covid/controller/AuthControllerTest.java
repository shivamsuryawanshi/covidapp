package covidapp.covid.controller;

import covidapp.covid.entity.User;
import covidapp.covid.repository.UserRepository;
import covidapp.covid.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    // ------------------------------------------------------
    // 1️⃣ TEST SIGNUP
    // ------------------------------------------------------
    @Test
    void testSignupSuccess() throws Exception {
        User user = new User("shivam", "123", "s@gmail.com");

        Mockito.when(userRepository.existsByUsername("shivam")).thenReturn(false);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.username").value("shivam"));
    }

    @Test
    void testSignupUserAlreadyExists() throws Exception {
        User user = new User("shivam", "123", "s@gmail.com");

        Mockito.when(userRepository.existsByUsername("shivam")).thenReturn(true);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Username already exists"));
    }

    @Test
    void testSignupMissingFields() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"shivam\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username, password, and email are required"));
    }

    // ------------------------------------------------------
    // 2️⃣ TEST LOGIN
    // ------------------------------------------------------
    @Test
    void testLoginSuccess() throws Exception {
        User user = new User("shivam", "123", "s@gmail.com");

        Mockito.when(userRepository.findByUsername("shivam"))
                .thenReturn(Optional.of(user));

        Mockito.doNothing().when(emailService).sendOtpEmail(anyString(), anyString());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"shivam\",\"password\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Password verified. OTP has been sent to your email."))
                .andExpect(jsonPath("$.username").value("shivam"));
    }

    @Test
    void testLoginInvalidCredentials() throws Exception {
        Mockito.when(userRepository.findByUsername("shivam"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"shivam\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid username or password"));
    }

    @Test
    void testLoginMissingFields() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"shivam\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username and password are required"));
    }

    // ------------------------------------------------------
    // 3️⃣ TEST VERIFY OTP
    // ------------------------------------------------------
    @Test
    void testVerifyOtpSuccess() throws Exception {
        User user = new User("shivam", "123", "s@gmail.com");
        user.setLogoutTimeoutMinutes(15);

        // Mock repository to return this user
        Mockito.when(userRepository.findByUsername("shivam"))
                .thenReturn(Optional.of(user));

        // 🔥 Insert OTP manually into private pendingOtps map
        AuthController controller = (AuthController)
                Objects.requireNonNull(mockMvc.getDispatcherServlet().getWebApplicationContext())
                        .getBean(AuthController.class);

        // Use reflection to access private field
        java.lang.reflect.Field otpField = AuthController.class.getDeclaredField("pendingOtps");
        otpField.setAccessible(true);
        Map<String, String> otpMap = (Map<String, String>) otpField.get(controller);

        // Put FIXED OTP
        otpMap.put("shivam", "123456");

        // Call verify-otp with correct OTP
        Map<String, String> body = new HashMap<>();
        body.put("username", "shivam");
        body.put("otp", "123456");

        mockMvc.perform(post("/api/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.username").value("shivam"))
                .andExpect(jsonPath("$.token").value("demo-token"));
    }

    // ------------------------------------------------------
    // 4️⃣ TEST SET LOGOUT TIMEOUT
    // ------------------------------------------------------
    @Test
    void testSetLogoutTimeoutSuccess() throws Exception {
        User user = new User("shivam", "123", "s@gmail.com");

        Mockito.when(userRepository.findByUsername("shivam"))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(user);

        mockMvc.perform(post("/api/auth/setLogoutTimeout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"shivam\",\"timeoutMinutes\":20}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Logout timeout updated successfully"))
                .andExpect(jsonPath("$.timeoutMinutes").value(20));
    }

    @Test
    void testSetLogoutTimeoutUserNotFound() throws Exception {
        Mockito.when(userRepository.findByUsername("shivam"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/setLogoutTimeout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"shivam\",\"timeoutMinutes\":20}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    void testSetLogoutTimeoutMissingFields() throws Exception {
        mockMvc.perform(post("/api/auth/setLogoutTimeout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"shivam\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Username and valid timeout minutes are required"));
    }
}
