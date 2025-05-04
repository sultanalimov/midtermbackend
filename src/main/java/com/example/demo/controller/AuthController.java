package com.example.demo.controller;


import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.AppUser;
import com.example.demo.repo.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.MyUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user login and registration")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final MyUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthController(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            JwtUtil jwtUtil,
            MyUserDetailsService userDetailsService,
            AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }



    // Login endpoint to authenticate user and generate JWT token
    @Operation(
            summary = "Login user and generate JWT token",
            description = "Authenticate a user using their username and password to generate a JWT token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful authentication", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) throws AuthenticationException {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        // Load user details from DB
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        // Generate JWT token
        String jwt = jwtUtil.generateToken(userDetails);

        // Return AuthResponse with token
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    // Register endpoint to create a new user (encode password before saving)
    @Operation(
            summary = "Register a new user",
            description = "Create a new user with a username and password, and return a JWT token for the newly registered user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful registration", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "User already exists", content = @Content)
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest authRequest) {
        // Check if user already exists
        if (userRepository.findByUsername(authRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(new AuthResponse("User already exists"));
        }

        // Encode the password before saving the new user
        String encodedPassword = passwordEncoder.encode(authRequest.getPassword());

        // Create new user and save
        AppUser newUser = new AppUser(authRequest.getUsername(), encodedPassword, "ROLE_USER");
        userRepository.save(newUser);

        // Generate JWT token for new user
        String jwt = jwtUtil.generateToken(userDetailsService.loadUserByUsername(authRequest.getUsername()));

        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}