package com.example.apifilme.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FilmeRequest(

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
        String titulo,

        @NotBlank(message = "O diretor é obrigatório")
        @Size(max = 120, message = "O diretor deve ter no máximo 120 caracteres")
        String diretor,

        @NotNull(message = "O ano é obrigatório")
        @Min(value = 1888, message = "O ano do filme não pode ser menor que 1888")
        Integer ano,

        @NotNull(message = "O id do gênero é obrigatório")
        Long generoId
) {
}
