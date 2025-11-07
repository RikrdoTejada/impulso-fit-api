package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "follows",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_follow_pair",
                columnNames = {"follower_id", "following_id"}
        )
)
public class Seguidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // quién sigue
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private Usuario follower;

    // a quién sigue
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private Usuario following;

    @Column(nullable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }

    public Seguidor() {}

    public Seguidor(Usuario follower, Usuario following) {
        this.follower = follower;
        this.following = following;
    }

    public Long getId() { return id; }
    public Usuario getFollower() { return follower; }
    public Usuario getFollowing() { return following; }
    public Instant getCreatedAt() { return createdAt; }

    public void setFollower(Usuario follower) { this.follower = follower; }
    public void setFollowing(Usuario following) { this.following = following; }
}