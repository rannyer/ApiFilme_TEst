package com.example.ApiFilme.Controller;


import com.example.ApiFilme.models.Filme;
import com.example.ApiFilme.repositories.FilmeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmeRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        repository.deleteAll();
    }


    @Test
    void deveCriarFilme() throws Exception{
        Filme filme = new Filme(null, "Senhor dos Spring: As duas APIs","Mr Oracle", 2002, "Fantasia" );

        mockMvc.perform(post("/filmes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filme)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Senhor dos Spring: As duas APIs"))
                .andExpect(jsonPath("$.diretor").value("Mr Oracle"))
                .andExpect(jsonPath("$.ano").value(2002))
                .andExpect(jsonPath("$.genero").value("Fantasia"));

    }
}
