package com.jwt.AegisAuth.controller;

import com.jwt.AegisAuth.dto.RoleUpdateDTO;
import com.jwt.AegisAuth.dto.UserDTO;
import com.jwt.AegisAuth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return authService.getAllUsers();
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String username){
        return ResponseEntity.ok(authService.searchUsers(username));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id){
        return ResponseEntity.ok(authService.getUserById(id));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<UserDTO> updateRole(
            @PathVariable String id,
            @Valid @RequestBody RoleUpdateDTO dto){
        return ResponseEntity.ok(authService.updateUserRole(id,dto));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id){
        authService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

}
