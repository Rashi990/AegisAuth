package com.jwt.AegisAuth.controller;

import com.jwt.AegisAuth.dto.RoleUpdateDTO;
import com.jwt.AegisAuth.dto.UserDTO;
import com.jwt.AegisAuth.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Validated
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/users/all")
    public List<UserDTO> getAllUsers() {
        return authService.getAllUsers();
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getUsers(

            @Min(0)
            @RequestParam(defaultValue = "0") int page,

            @Min(1)
            @Max(100)
            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "username") String sort,

            @RequestParam(defaultValue = "asc") String direction,

            @RequestParam(required = false) String role,

            @RequestParam(required = false) String username,

            @RequestParam(required = false) String email){

        return ResponseEntity.ok(authService.getUsers(page, size, sort, direction, role, username, email));
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
