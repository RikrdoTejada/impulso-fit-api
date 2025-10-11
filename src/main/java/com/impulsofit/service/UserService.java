package com.impulsofit.service;

import com.impulsofit.dto.request.UserRequest;
import com.impulsofit.dto.response.UserResponse;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.User;
import com.impulsofit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponse create(UserRequest u) {
        User user = new User(u.username(), u.email(), u.password());

        // si no se seteó, poner ahora usando LocalDateTime (NO Instant)
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        User saved = repository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getPassword(),
                saved.getCreatedAt()
        );
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        repository.deleteById(id);
    }
}