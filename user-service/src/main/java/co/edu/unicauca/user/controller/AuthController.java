package co.edu.unicauca.user.controller;
import co.edu.unicauca.user.dto.*;
import co.edu.unicauca.user.service.UserService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController public class AuthController {
 private final UserService users;
 public AuthController(UserService users) { this.users=users; }
 @PostMapping("/auth/login") public LoginResponse login(@RequestBody LoginRequest request) { return users.login(request); }
 @GetMapping("/auth/verify") public UserProfile verify(@RequestHeader(value="Authorization",required=false) String token) { return users.verify(token); }
 @GetMapping("/users/{id}") public UserProfile user(@PathVariable Long id) { return users.findById(id); }
}
