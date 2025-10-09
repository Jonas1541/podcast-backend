package com.positivo.podcast.controllers;

import com.positivo.podcast.dtos.request.PodcastRequestDto;
import com.positivo.podcast.dtos.response.PodcastResponseDto;
import com.positivo.podcast.services.PodcastService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/podcasts")
public class PodcastController {

    @Autowired
    private PodcastService podcastService;

    @GetMapping
    public ResponseEntity<List<PodcastResponseDto>> findAll() {
        List<PodcastResponseDto> list = podcastService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PodcastResponseDto> findById(@PathVariable Long id) {
        PodcastResponseDto dto = podcastService.findById(id);
        return ResponseEntity.ok(dto);
    }
    
    @PostMapping
    public ResponseEntity<PodcastResponseDto> create(@RequestBody @Valid PodcastRequestDto createDto) {
        PodcastResponseDto createdDto = podcastService.create(createDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdDto.id()).toUri();
        return ResponseEntity.created(uri).body(createdDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PodcastResponseDto> update(@PathVariable Long id, @RequestBody @Valid PodcastRequestDto updateDto) {
        PodcastResponseDto updatedDto = podcastService.update(id, updateDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        podcastService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
