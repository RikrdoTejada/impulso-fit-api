package com.impulsofit.controller;

import com.impulsofit.dto.request.UserRequest;
import com.impulsofit.dto.response.UserResponse;
import com.impulsofit.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users") // <- usa solo '/users' si tienes context-path=/api/v1
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest u) {
        return ResponseEntity.status(201).body(userService.create(u));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> list() {
        return ResponseEntity.ok(userService.list());
    }

    @PutMapping("/{id}")                            // <-- NUEVO
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @RequestBody UserRequest u
    ) {
        return ResponseEntity.ok(userService.update(id, u));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}