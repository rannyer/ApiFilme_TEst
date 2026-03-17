package com.example.apifilme.Controller;

import com.example.apifilme.dto.FilmeRequest;
import com.example.apifilme.models.Genero;
import com.example.apifilme.repository.FilmeRepository;
import com.example.apifilme.repository.GeneroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmeControllerIntegrationTest {
    private MockMvc mockMvc;
    private GeneroRepository generoRepository;
    private FilmeRepository filmeRepository;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        filmeRepository.deleteAll();
        generoRepository.deleteAll();
    }

    void CT001_deveCriarFilmeComSucesso() throws Exception{
        // ARRANGE
        Genero genero = new Genero();
        genero.setNome("Ação");
        Genero genersoSalvo = generoRepository.save(genero);

        FilmeRequest request = new FilmeRequest(
                "Java: Ultimato",
                "Mr Oracle",
                2019,
                genersoSalvo.getId()
        );

        //ACT+assert
        mockMvc.perform(post("/filmes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.titulo").value("Java: Ultimato"))
                .andExpect(jsonPath("$.diretor").value("Mr Oracle"))
                .andExpect(jsonPath("$.ano").value(2019))
                .andExpect(jsonPath("$.genero.id").value(genersoSalvo.getId()));

        //ASSERT
        assertEquals(1, filmeRepository.count());
    }
    // náo deve criar filme com ano futuro
}
