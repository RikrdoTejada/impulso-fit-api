package com.impulsofit.service;

import com.impulsofit.model.Group;
import com.impulsofit.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository repo;

    public List<Group> listar() { return repo.findAll(); }
    public Group crear(Group g) { return repo.save(g); }
}

