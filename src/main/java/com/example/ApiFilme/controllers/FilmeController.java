package com.example.ApiFilme.controllers;

import com.example.ApiFilme.models.Filme;
import com.example.ApiFilme.repositories.FilmeRepository;
import com.example.ApiFilme.services.FilmeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filmes")
public class FilmeController {
    private final FilmeService service;

    public FilmeController(FilmeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Filme>> listar(){
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Filme> buscarPorId(@PathVariable  Long id){
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Filme> salvar(@RequestBody Filme filme){
        Filme salvo = service.salvar(filme);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        boolean deletado = service.deletarPorId(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
