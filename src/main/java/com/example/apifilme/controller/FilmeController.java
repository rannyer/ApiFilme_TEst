package com.example.apifilme.controller;

import com.example.apifilme.dto.FilmeRequest;
import com.example.apifilme.dto.FilmeResponse;
import com.example.apifilme.service.FilmeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filmes")
public class FilmeController {

    private final FilmeService filmeService;

    public FilmeController(FilmeService filmeService) {
        this.filmeService = filmeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmeResponse criar(@RequestBody @Valid FilmeRequest request) {
        return filmeService.criar(request);
    }

    @GetMapping
    public List<FilmeResponse> listarTodos() {
        return filmeService.listarTodos();
    }

    @GetMapping("/{id}")
    public FilmeResponse buscarPorId(@PathVariable Long id) {
        return filmeService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public FilmeResponse atualizar(@PathVariable Long id, @RequestBody @Valid FilmeRequest request) {
        return filmeService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        filmeService.deletar(id);
    }

    @GetMapping("/filtro")
    public List<FilmeResponse> filtrar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String diretor,
            @RequestParam(required = false) Integer anoMin,
            @RequestParam(required = false) Integer anoMax,
            @RequestParam(required = false) Long generoId,
            @RequestParam(required = false) String generoNome
    ) {
        return filmeService.filtrar(titulo, diretor, anoMin, anoMax, generoId, generoNome);
    }
}
