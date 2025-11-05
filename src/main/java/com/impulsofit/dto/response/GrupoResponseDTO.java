package com.impulsofit.dto.response;

import java.time.Instant;

public class GrupoResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Long createdById;
    private String createdByUsername;
    private Instant createdAt;

    public GrupoResponseDTO(Long id, String name, String description,
                            Long createdById, String createdByUsername, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdById = createdById;
        this.createdByUsername = createdByUsername;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Long getCreatedById() { return createdById; }
    public String getCreatedByUsername() { return createdByUsername; }
    public Instant getCreatedAt() { return createdAt; }
}
