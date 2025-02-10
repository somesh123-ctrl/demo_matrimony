package com.matrimony.Controller;

import com.matrimony.Entity.User;
import com.matrimony.Service.UserService;
import com.matrimony.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Sign Up (Registers a new user with password hashing)
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        try {
            return userService.addUser(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error during registration: " + e.getMessage());
        }
    }

    // ✅ Login (Authenticates user and returns JWT token)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        // Authenticate user
        ResponseEntity<?> response = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (response.getStatusCode() != HttpStatus.OK) {
            return response;
        }

        // Successful login, create JWT token
        User user = (User) response.getBody();
        String token = jwtUtil.generateToken(user.getEmail());

        // Prepare login response
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("user", user);

        return ResponseEntity.ok(responseBody);
    }

    // ✅ Update user details
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
    	System.out.println(updatedUser);
        return userService.updateUser(id, updatedUser);
    }
    
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
