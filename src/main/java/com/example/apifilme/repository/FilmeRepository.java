package com.example.apifilme.repository;

import com.example.apifilme.models.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FilmeRepository extends JpaRepository<Filme, Long>, JpaSpecificationExecutor<Filme> {
    boolean existsByTituloIgnoreCaseAndDiretorIgnoreCaseAndAno(String titulo, String diretor, Integer ano);
}
