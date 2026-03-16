package com.example.ApiFilme.services;

import com.example.ApiFilme.models.Filme;
import com.example.ApiFilme.repositories.FilmeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmeService {
    private final FilmeRepository repository;


    public FilmeService(FilmeRepository repository) {
        this.repository = repository;
    }

    public Filme salvar(Filme filme){
        return repository.save(filme);
    }

    public List<Filme> listar(){
        return repository.findAll();
    }

    public Optional<Filme> buscarPorId(Long id){
        return repository.findById(id);
    }

    public boolean deletarPorId(Long id){
        if(!repository.existsById(id)){
            return false;
        }
        repository.deleteById(id);
        return true;
    }










}
