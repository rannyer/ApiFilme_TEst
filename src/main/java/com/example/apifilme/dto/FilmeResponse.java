package com.example.apifilme.dto;

public record FilmeResponse(
        Long id,
        String titulo,
        String diretor,
        Integer ano,
        Long generoId,
        String generoNome
) {
}
