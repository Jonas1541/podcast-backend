package com.positivo.podcast.services;

import com.positivo.podcast.dtos.request.PodcastRequestDto;
import com.positivo.podcast.dtos.response.PodcastResponseDto;
import com.positivo.podcast.entities.Podcast;
import com.positivo.podcast.exceptions.ResourceNotFoundException;
import com.positivo.podcast.repositories.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PodcastService {

    @Autowired
    private PodcastRepository podcastRepository;

    // Injetaremos um serviço de upload de arquivos aqui no futuro.
    // @Autowired
    // private FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<PodcastResponseDto> findAll() {
        return podcastRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PodcastResponseDto findById(Long id) {
        Podcast podcast = podcastRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Podcast não encontrado com o id: " + id));
        return toDto(podcast);
    }
    
    @Transactional
    public PodcastResponseDto create(PodcastRequestDto podcastDto) {
        Podcast podcast = new Podcast();
        podcast.setTitulo(podcastDto.titulo());
        podcast.setDescricao(podcastDto.descricao());
        podcast.setCapaUrl(podcastDto.capaUrl());
        podcast.setAudioUrl(podcastDto.audioUrl());

        Podcast savedPodcast = podcastRepository.save(podcast);
        return toDto(savedPodcast);
    }

    @Transactional
    public PodcastResponseDto update(Long id, PodcastRequestDto podcastDto) {
        Podcast podcast = podcastRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Podcast não encontrado com o id: " + id));
        
        podcast.setTitulo(podcastDto.titulo());
        podcast.setDescricao(podcastDto.descricao());
        podcast.setCapaUrl(podcastDto.capaUrl());
        podcast.setAudioUrl(podcastDto.audioUrl());
        
        Podcast updatedPodcast = podcastRepository.save(podcast);
        return toDto(updatedPodcast);
    }

    @Transactional
    public void delete(Long id) {
        if (!podcastRepository.existsById(id)) {
            throw new ResourceNotFoundException("Podcast não encontrado com o id: " + id);
        }
        // Lógica de deletar arquivos do storage viria aqui antes de deletar do banco
        // Podcast podcast = podcastRepository.findById(id).get();
        // fileStorageService.delete(podcast.getAudioUrl());
        // fileStorageService.delete(podcast.getCapaUrl());

        podcastRepository.deleteById(id);
    }

    // Método helper para converter Entidade para DTO
    private PodcastResponseDto toDto(Podcast podcast) {
        return new PodcastResponseDto(
            podcast.getId(),
            podcast.getTitulo(),
            podcast.getDescricao(),
            podcast.getCapaUrl(),
            podcast.getAudioUrl()
        );
    }
}
