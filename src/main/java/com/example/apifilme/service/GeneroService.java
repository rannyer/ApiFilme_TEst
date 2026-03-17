package com.example.apifilme.service;

import com.example.apifilme.dto.GeneroRequest;
import com.example.apifilme.dto.GeneroResponse;
import com.example.apifilme.exception.ConflictException;
import com.example.apifilme.exception.NotFoundException;
import com.example.apifilme.models.Genero;
import com.example.apifilme.repository.GeneroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneroService {

    private final GeneroRepository generoRepository;

    public GeneroService(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    public GeneroResponse criar(GeneroRequest request) {
        String nomeTratado = request.nome().trim();

        if (generoRepository.existsByNomeIgnoreCase(nomeTratado)) {
            throw new ConflictException("Já existe um gênero com esse nome");
        }

        Genero genero = new Genero();
        genero.setNome(nomeTratado);

        Genero salvo = generoRepository.save(genero);
        return toResponse(salvo);
    }

    public List<GeneroResponse> listar() {
        return generoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public GeneroResponse buscarPorId(Long id) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gênero não encontrado"));
        return toResponse(genero);
    }

    public GeneroResponse atualizar(Long id, GeneroRequest request) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gênero não encontrado"));

        String nomeTratado = request.nome().trim();

        generoRepository.findByNomeIgnoreCase(nomeTratado)
                .filter(g -> !g.getId().equals(id))
                .ifPresent(g -> {
                    throw new ConflictException("Já existe outro gênero com esse nome");
                });

        genero.setNome(nomeTratado);
        return toResponse(generoRepository.save(genero));
    }

    public void deletar(Long id) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gênero não encontrado"));

        generoRepository.delete(genero);
    }

    public Genero buscarEntidadePorId(Long id) {
        return generoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gênero não encontrado"));
    }

    private GeneroResponse toResponse(Genero genero) {
        return new GeneroResponse(
                genero.getId(),
                genero.getNome()
        );
    }
}
