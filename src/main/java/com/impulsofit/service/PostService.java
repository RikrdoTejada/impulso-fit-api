package com.impulsofit.service;

import com.impulsofit.dto.request.CreatePostRequest;
import com.impulsofit.dto.response.PostResponse;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Challenge;
import com.impulsofit.model.Group;
import com.impulsofit.model.Post;
import com.impulsofit.model.User;
import com.impulsofit.repository.ChallengeRepository;
import com.impulsofit.repository.GroupRepository;
import com.impulsofit.repository.PostRepository;
import com.impulsofit.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class PostService {

    private final PostRepository posts;
    private final UserRepository users;
    private final GroupRepository groups;
    private final ChallengeRepository challenges;

    public PostService(PostRepository posts, UserRepository users, GroupRepository groups, ChallengeRepository challenges) {
        this.posts = posts;
        this.users = users;
        this.groups = groups;
        this.challenges = challenges;
    }

    @Transactional
    public PostResponse create(CreatePostRequest req) {
        // Para record: req.userId(), req.content(), req.challengeId(), req.groupId()
        User user = users.findById(req.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + req.userId()));

        Post post = new Post();
        post.setUser(user);
        post.setContent(req.content());
        post.setCreatedAt(Instant.now());

        if (req.challengeId() != null) {
            Challenge ch = challenges.findById(req.challengeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Challenge not found with id: " + req.challengeId()));
            post.setChallenge(ch);
            post.setGroup(ch.getGroup()); // satisface NOT NULL en grupo_id
        } else if (req.groupId() != null) {
            Group g = groups.findById(req.groupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + req.groupId()));
            post.setGroup(g);
        } else {
            throw new IllegalArgumentException("Post must belong to a group or challenge");
        }

        Post saved = posts.save(post);

        return new PostResponse(
                saved.getId(),
                saved.getUser().getId(),
                saved.getContent(),
                saved.getChallenge() != null ? saved.getChallenge().getId() : null,
                saved.getGroup().getId(),
                saved.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<PostResponse> listAll() {
        return posts.findAll().stream()
                .map(p -> new PostResponse(
                        p.getId(),
                        p.getUser().getId(),
                        p.getContent(),
                        p.getChallenge() != null ? p.getChallenge().getId() : null,
                        p.getGroup().getId(),
                        p.getCreatedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> listByUser(Long userId) {
        // Validar que el usuario exista (opcional pero coherente)
        users.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return posts.findAllByUserId(userId).stream()
                .map(p -> new PostResponse(
                        p.getId(),
                        p.getUser().getId(),
                        p.getContent(),
                        p.getChallenge() != null ? p.getChallenge().getId() : null,
                        p.getGroup().getId(),
                        p.getCreatedAt()
                ))
                .toList();
    }
}