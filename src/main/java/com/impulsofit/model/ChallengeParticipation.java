package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "challenge_participations",
        uniqueConstraints = @UniqueConstraint(name = "uk_challenge_user", columnNames = {"challenge_id", "user_id"})
)
public class ChallengeParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "challenge_id", nullable = false)
    private Long challengeId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Instant joinedAt = Instant.now();

    // getters/setters
    public Long getId() { return id; }
    public Long getChallengeId() { return challengeId; }
    public Long getUserId() { return userId; }
    public Instant getJoinedAt() { return joinedAt; }

    public void setChallengeId(Long challengeId) { this.challengeId = challengeId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setJoinedAt(Instant joinedAt) { this.joinedAt = joinedAt; }
}