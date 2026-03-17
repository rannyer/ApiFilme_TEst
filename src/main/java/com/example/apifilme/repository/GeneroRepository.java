package com.example.apifilme.repository;

import com.example.apifilme.models.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeneroRepository extends JpaRepository<Genero, Long> {
    Optional<Genero> findByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);
}
