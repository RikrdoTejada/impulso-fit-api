package com.impulsofit.service;

import com.impulsofit.dto.response.FollowResponse;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Follow;
import com.impulsofit.model.User;
import com.impulsofit.repository.FollowRepository;
import com.impulsofit.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public FollowResponse follow(Long followerId, Long targetId) {
        if (followerId.equals(targetId)) {
            throw new IllegalArgumentException("No puedes seguirte a ti mismo.");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("User (follower) not found: " + followerId));

        User following = userRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException("User (target) not found: " + targetId));

        if (followRepository.existsByFollower_IdAndFollowing_Id(followerId, targetId)) {
            // ya existe, devolvemos la relación actual
            Follow existing = followRepository.findByFollower_IdAndFollowing_Id(followerId, targetId).get();
            return toResponse(existing);
        }

        Follow saved = followRepository.save(new Follow(follower, following));
        return toResponse(saved);
    }

    @Transactional
    public void unfollow(Long followerId, Long targetId) {
        Follow f = followRepository.findByFollower_IdAndFollowing_Id(followerId, targetId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Follow relation not found (follower=" + followerId + ", following=" + targetId + ")"));

        followRepository.delete(f);
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> followersOf(Long userId) {
        // quiénes siguen a userId
        return followRepository.findByFollowing_Id(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> followingOf(Long userId) {
        // a quién sigue userId
        return followRepository.findByFollower_Id(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    private FollowResponse toResponse(Follow f) {
        return new FollowResponse(
                f.getId(),
                f.getFollower().getId(),
                f.getFollowing().getId(),
                f.getCreatedAt()
        );
    }
}