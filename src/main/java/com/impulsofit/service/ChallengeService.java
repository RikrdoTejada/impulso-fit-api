package com.impulsofit.service;

import com.impulsofit.dto.request.CreateChallengeRequest;
import com.impulsofit.dto.response.ChallengeResponse;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Challenge;
import com.impulsofit.model.Group;
import com.impulsofit.model.User;
import com.impulsofit.repository.ChallengeRepository;
import com.impulsofit.repository.GroupRepository;
import com.impulsofit.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public ChallengeService(ChallengeRepository challengeRepository,
                            UserRepository userRepository,
                            GroupRepository groupRepository) {
        this.challengeRepository = challengeRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public ChallengeResponse create(CreateChallengeRequest req) {
        if (req.groupId() == null) {
            throw new IllegalArgumentException("groupId is required");
        }
        if (req.createdById() == null) {
            throw new IllegalArgumentException("createdById is required");
        }

        User createdBy = userRepository.findById(req.createdById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + req.createdById()));

        Group group = groupRepository.findById(req.groupId())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + req.groupId()));

        Challenge ch = new Challenge();
        ch.setTitle(req.title());
        ch.setDescription(req.description());
        ch.setStartDate(req.startDate());
        ch.setEndDate(req.endDate());
        ch.setCreatedAt(Instant.now());
        ch.setCreatedBy(createdBy);
        ch.setGroup(group);

        Challenge saved = challengeRepository.save(ch);

        return new ChallengeResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getStartDate(),
                saved.getEndDate(),
                saved.getCreatedAt(),
                saved.getCreatedBy().getId(),
                saved.getGroup().getId()
        );
    }
}