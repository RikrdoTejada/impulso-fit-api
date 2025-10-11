package com.impulsofit.controller;

import com.impulsofit.dto.request.CreateGroupRequest;
import com.impulsofit.dto.response.GroupResponse;
import com.impulsofit.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<GroupResponse> create(@Valid @RequestBody CreateGroupRequest req) {
        GroupResponse response = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}