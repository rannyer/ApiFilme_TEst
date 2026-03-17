package com.example.apifilme.controller;

import com.example.apifilme.dto.GeneroRequest;
import com.example.apifilme.dto.GeneroResponse;
import com.example.apifilme.service.GeneroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/generos")
public class GeneroController {

    private final GeneroService generoService;

    public GeneroController(GeneroService generoService) {
        this.generoService = generoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GeneroResponse criar(@RequestBody @Valid GeneroRequest request) {
        return generoService.criar(request);
    }

    @GetMapping
    public List<GeneroResponse> listar() {
        return generoService.listar();
    }

    @GetMapping("/{id}")
    public GeneroResponse buscarPorId(@PathVariable Long id) {
        return generoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public GeneroResponse atualizar(@PathVariable Long id, @RequestBody @Valid GeneroRequest request) {
        return generoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        generoService.deletar(id);
    }
}
