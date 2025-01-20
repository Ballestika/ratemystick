package com.ratemystick.ratemystick.repos;

import com.ratemystick.ratemystick.domain.Anuncio;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {
}
