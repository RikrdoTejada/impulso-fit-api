package com.impulsofit.service;

import com.impulsofit.dto.request.CreateGroupRequest;
import com.impulsofit.dto.response.GroupResponse;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Group;
import com.impulsofit.model.User;
import com.impulsofit.repository.GroupRepository;
import com.impulsofit.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class GroupService {

    private final GroupRepository groups;
    private final UserRepository users;

    public GroupService(GroupRepository groups, UserRepository users) {
        this.groups = groups;
        this.users = users;
    }

    @Transactional
    public GroupResponse create(CreateGroupRequest req) {
        User creator = users.findById(req.getCreatedById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + req.getCreatedById()));

        Group g = new Group();
        g.setName(req.getName());
        g.setDescription(req.getDescription());
        g.setCreatedBy(creator);
        g.setCreatedAt(Instant.now());

        Group saved = groups.save(g);

        return new GroupResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getCreatedBy().getId(),
                saved.getCreatedBy().getUsername(),
                saved.getCreatedAt()
        );
    }
}

