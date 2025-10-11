package com.impulsofit.service;

import com.impulsofit.dto.request.UserRequest;
import com.impulsofit.dto.response.UserResponse;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.User;
import com.impulsofit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserResponse create(UserRequest u) {
        User user = new User();
        user.setUsername(u.username());
        user.setEmail(u.email());
        user.setPassword(u.password());
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(Instant.now());
        }
        User saved = repository.save(user);
        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getCreatedAt()
        );
    }

    @Override
    public UserResponse update(Long id, UserRequest u) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Para PUT: reemplaza todos los campos
        user.setUsername(u.username());
        user.setEmail(u.email());
        user.setPassword(u.password());
        // createdAt se mantiene

        User saved = repository.save(user);
        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getCreatedAt()
        );
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public UserResponse getById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

    @Override
    public List<UserResponse> list() {
        return repository.findAll()
                .stream()
                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getCreatedAt()))
                .toList();
    }
}