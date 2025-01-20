package com.ratemystick.ratemystick.rest;

import com.ratemystick.ratemystick.model.AnuncioDTO;
import com.ratemystick.ratemystick.service.AnuncioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/anuncios", produces = MediaType.APPLICATION_JSON_VALUE)
public class AnuncioResource {

    private final AnuncioService anuncioService;

    public AnuncioResource(final AnuncioService anuncioService) {
        this.anuncioService = anuncioService;
    }

    @GetMapping
    public ResponseEntity<List<AnuncioDTO>> getAllAnuncios() {
        return ResponseEntity.ok(anuncioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnuncioDTO> getAnuncio(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(anuncioService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createAnuncio(@RequestBody @Valid final AnuncioDTO anuncioDTO) {
        final Long createdId = anuncioService.create(anuncioDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAnuncio(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AnuncioDTO anuncioDTO) {
        anuncioService.update(id, anuncioDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnuncio(@PathVariable(name = "id") final Long id) {
        anuncioService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
