package com.impulsofit.controller;

import com.impulsofit.dto.request.ReaccionRequest;
import com.impulsofit.dto.response.ReaccionResponse;
import com.impulsofit.service.ReaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reaction")
@RequiredArgsConstructor
public class ReaccionController {
    private final ReaccionService reaccionService;

    @PostMapping
    public ResponseEntity<ReaccionResponse> create(@RequestBody ReaccionRequest r) {
        ReaccionResponse saved = reaccionService.create(r);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ReaccionResponse> delete(@PathVariable Long id) {
        reaccionService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
