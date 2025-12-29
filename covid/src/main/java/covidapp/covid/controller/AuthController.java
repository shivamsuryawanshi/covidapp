package covidapp.covid.controller;

import covidapp.covid.entity.User;
import covidapp.covid.repository.UserRepository;
import covidapp.covid.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Value("${logout.timeout.minutes:30}")
    private int defaultLogoutTimeoutMinutes;

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final Map<String, String> pendingOtps = new ConcurrentHashMap<>();

    public AuthController(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody User request) {
        Map<String, Object> errorResponse = new HashMap<>();
        if (request.getUsername() == null || request.getPassword() == null || request.getEmail() == null) {
            errorResponse.put("error", "Username, password, and email are required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            errorResponse.put("error", "Username already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        User user = new User(request.getUsername(), request.getPassword(), request.getEmail());
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("username", user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User request) {
        Map<String, Object> errorResponse = new HashMap<>();
        if (request.getUsername() == null || request.getPassword() == null) {
            errorResponse.put("error", "Username and password are required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        String requestedUsername = request.getUsername().trim();
        String requestedPassword = request.getPassword();

        return userRepository.findByUsername(requestedUsername)
                .filter(user -> {
                    // Safe password comparison with null checks
                    String storedPassword = user.getPassword();
                    if (storedPassword == null || requestedPassword == null) {
                        System.err.println("Login attempt failed: null password for username: " + requestedUsername);
                        return false;
                    }
                    // Compare passwords (exact match, no trimming to preserve original password)
                    boolean matches = storedPassword.equals(requestedPassword);
                    if (!matches) {
                        System.err.println("Login attempt failed: password mismatch for username: " + requestedUsername);
                    }
                    return matches;
                })
                .map(user -> {
                    // Generate 6-digit OTP
                    String otp = String.format("%06d", new Random().nextInt(1_000_000));
                    pendingOtps.put(user.getUsername(), otp);

                    // Send OTP via email (non-blocking - continue even if email fails)
                    boolean emailSent = false;
                    try {
                        emailService.sendOtpEmail(user.getEmail(), otp);
                        emailSent = true;
                    } catch (Exception e) {
                        System.err.println("Failed to send OTP email: " + e.getMessage());
                        System.err.println("OTP for username '" + user.getUsername() + "' is: " + otp);
                        // Continue with login flow even if email fails (for testing purposes)
                    }

                    Map<String, Object> response = new HashMap<>();
                    if (emailSent) {
                        response.put("message", "Password verified. OTP has been sent to your email.");
                    } else {
                        response.put("message", "Password verified. OTP generated (email service unavailable).");
                        // Log OTP to console for testing
                        System.out.println("=== OTP FOR TESTING ===");
                        System.out.println("Username: " + user.getUsername());
                        System.out.println("OTP: " + otp);
                        System.out.println("======================");
                    }
                    response.put("username", user.getUsername());
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    // Check if user exists to provide better error message
                    Optional<User> userOpt = userRepository.findByUsername(requestedUsername);
                    if (userOpt.isPresent()) {
                        errorResponse.put("error", "Invalid password");
                    } else {
                        errorResponse.put("error", "User not found");
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
                });
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        Map<String, Object> errorResponse = new HashMap<>();
        String username = request.get("username");
        String otp = request.get("otp");

        if (username == null || otp == null) {
            errorResponse.put("error", "Username and OTP are required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        String expectedOtp = pendingOtps.get(username);

        if (expectedOtp != null && expectedOtp.equals(otp)) {
            pendingOtps.remove(username);

            // Get user to check for custom timeout
            User user = userRepository.findByUsername(username).orElse(null);
            int timeoutMinutes = (user != null && user.getLogoutTimeoutMinutes() != null)
                    ? user.getLogoutTimeoutMinutes()
                    : defaultLogoutTimeoutMinutes;

            LocalDateTime loginTime = LocalDateTime.now();
            LocalDateTime expiryTime = loginTime.plusMinutes(timeoutMinutes);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("username", username);
            response.put("token", "demo-token");
            response.put("loginTime", loginTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            response.put("expiryTime", expiryTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            response.put("timeoutMinutes", timeoutMinutes);

            return ResponseEntity.ok(response);
        }

        errorResponse.put("error", "Invalid or expired OTP");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @PostMapping("/setLogoutTimeout")
    public ResponseEntity<Map<String, Object>> setLogoutTimeout(@RequestBody Map<String, Object> request) {

        String username = (String) request.get("username");
        Integer timeoutMinutes = (Integer) request.get("timeoutMinutes");

        Map<String, Object> errorResponse = new HashMap<>();
        if (username == null || timeoutMinutes == null || timeoutMinutes <= 0) {
            errorResponse.put("error", "Username and valid timeout minutes are required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        return userRepository.findByUsername(username)
                .map(user -> {
                    user.setLogoutTimeoutMinutes(timeoutMinutes);
                    userRepository.save(user);

                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Logout timeout updated successfully");
                    response.put("username", username);
                    response.put("timeoutMinutes", timeoutMinutes);

                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    errorResponse.put("error", "User not found");
                    return ResponseEntity.badRequest().body(errorResponse);
                });
    }




}