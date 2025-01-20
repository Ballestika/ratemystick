package com.ratemystick.ratemystick.service;

import com.ratemystick.ratemystick.domain.Anuncio;
import com.ratemystick.ratemystick.model.AnuncioDTO;
import com.ratemystick.ratemystick.repos.AnuncioRepository;
import com.ratemystick.ratemystick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AnuncioService {

    private final AnuncioRepository anuncioRepository;

    public AnuncioService(final AnuncioRepository anuncioRepository) {
        this.anuncioRepository = anuncioRepository;
    }

    public List<AnuncioDTO> findAll() {
        final List<Anuncio> anuncios = anuncioRepository.findAll(Sort.by("id"));
        return anuncios.stream()
                .map(anuncio -> mapToDTO(anuncio, new AnuncioDTO()))
                .toList();
    }

    public AnuncioDTO get(final Long id) {
        return anuncioRepository.findById(id)
                .map(anuncio -> mapToDTO(anuncio, new AnuncioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AnuncioDTO anuncioDTO) {
        final Anuncio anuncio = new Anuncio();
        mapToEntity(anuncioDTO, anuncio);
        return anuncioRepository.save(anuncio).getId();
    }

    public void update(final Long id, final AnuncioDTO anuncioDTO) {
        final Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(anuncioDTO, anuncio);
        anuncioRepository.save(anuncio);
    }

    public void delete(final Long id) {
        anuncioRepository.deleteById(id);
    }

    private AnuncioDTO mapToDTO(final Anuncio anuncio, final AnuncioDTO anuncioDTO) {
        anuncioDTO.setId(anuncio.getId());
        anuncioDTO.setClicks(anuncio.getClicks());
        return anuncioDTO;
    }

    private Anuncio mapToEntity(final AnuncioDTO anuncioDTO, final Anuncio anuncio) {
        anuncio.setClicks(anuncioDTO.getClicks());
        return anuncio;
    }

}
