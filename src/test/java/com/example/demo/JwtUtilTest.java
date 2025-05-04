package com.example.demo;

import com.example.demo.security.JwtUtil;
import com.example.demo.service.MyUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private MyUserDetailsService userDetailsService;

    @Test
    void generateAndValidateToken() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("testpass")
                .roles("USER")
                .build();

        String token = jwtUtil.generateToken(userDetails);

        assertNotNull(token);
        assertEquals("testuser", jwtUtil.extractUsername(token));
        assertTrue(jwtUtil.isTokenValid(token, userDetails));
    }
}
