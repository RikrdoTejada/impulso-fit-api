package com.impulsofit.controller;

import com.impulsofit.dto.request.RespuestaRequest;
import com.impulsofit.dto.response.RespuestaResponse;
import com.impulsofit.model.Respuesta;
import com.impulsofit.service.RespuestaService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
public class RespuestaController {
    private final RespuestaService respuestaService;

    @PostMapping
    public ResponseEntity<RespuestaResponse> create(@RequestBody RespuestaRequest r){
        RespuestaResponse saved = respuestaService.create(r);
        return ResponseEntity.ok(saved);
    }
}
