package com.impulsofit.service;

import com.impulsofit.model.User;
import com.impulsofit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;

    public List<User> listar() { return repo.findAll(); }
    public User crear(User u) { return repo.save(u); }

    // “Eliminar cuenta” (baja lógica)
    @Transactional
    public void darDeBaja(Long id) {
        repo.findById(id).ifPresent(u -> u.setActivo(false));
    }
}
