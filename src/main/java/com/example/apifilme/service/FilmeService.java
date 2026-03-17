package com.example.apifilme.service;

import com.example.apifilme.dto.FilmeRequest;
import com.example.apifilme.dto.FilmeResponse;
import com.example.apifilme.exception.ConflictException;
import com.example.apifilme.exception.NotFoundException;
import com.example.apifilme.models.Filme;
import com.example.apifilme.models.Genero;
import com.example.apifilme.repository.FilmeRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilmeService {

    private final FilmeRepository filmeRepository;
    private final GeneroService generoService;

    public FilmeService(FilmeRepository filmeRepository, GeneroService generoService) {
        this.filmeRepository = filmeRepository;
        this.generoService = generoService;
    }

    public FilmeResponse criar(FilmeRequest request) {
        validarAno(request.ano());

        String titulo = request.titulo().trim();
        String diretor = request.diretor().trim();

        if (filmeRepository.existsByTituloIgnoreCaseAndDiretorIgnoreCaseAndAno(
                titulo, diretor, request.ano()
        )) {
            throw new ConflictException("Já existe um filme com o mesmo título, diretor e ano");
        }

        Genero genero = generoService.buscarEntidadePorId(request.generoId());

        Filme filme = new Filme();
        filme.setTitulo(titulo);
        filme.setDiretor(diretor);
        filme.setAno(request.ano());
        filme.setGenero(genero);

        Filme salvo = filmeRepository.save(filme);
        return toResponse(salvo);
    }

    public List<FilmeResponse> listarTodos() {
        return filmeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public FilmeResponse buscarPorId(Long id) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Filme não encontrado"));
        return toResponse(filme);
    }

    public FilmeResponse atualizar(Long id, FilmeRequest request) {
        validarAno(request.ano());

        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Filme não encontrado"));

        Genero genero = generoService.buscarEntidadePorId(request.generoId());

        String titulo = request.titulo().trim();
        String diretor = request.diretor().trim();

        boolean existeDuplicado = filmeRepository.existsByTituloIgnoreCaseAndDiretorIgnoreCaseAndAno(
                titulo, diretor, request.ano()
        );

        boolean mudouChaveNegocio =
                !filme.getTitulo().equalsIgnoreCase(titulo) ||
                !filme.getDiretor().equalsIgnoreCase(diretor) ||
                !filme.getAno().equals(request.ano());

        if (existeDuplicado && mudouChaveNegocio) {
            throw new ConflictException("Já existe um filme com o mesmo título, diretor e ano");
        }

        filme.setTitulo(titulo);
        filme.setDiretor(diretor);
        filme.setAno(request.ano());
        filme.setGenero(genero);

        Filme atualizado = filmeRepository.save(filme);
        return toResponse(atualizado);
    }

    public void deletar(Long id) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Filme não encontrado"));

        filmeRepository.delete(filme);
    }

    public List<FilmeResponse> filtrar(
            String titulo,
            String diretor,
            Integer anoMin,
            Integer anoMax,
            Long generoId,
            String generoNome
    ) {
        if (anoMin != null && anoMax != null && anoMin > anoMax) {
            throw new IllegalArgumentException("anoMin não pode ser maior que anoMax");
        }

        if (anoMin != null) {
            validarAno(anoMin);
        }

        if (anoMax != null) {
            validarAno(anoMax);
        }

        Specification<Filme> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (titulo != null && !titulo.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("titulo")),
                        "%" + titulo.toLowerCase() + "%"
                ));
            }

            if (diretor != null && !diretor.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("diretor")),
                        "%" + diretor.toLowerCase() + "%"
                ));
            }

            if (anoMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("ano"), anoMin));
            }

            if (anoMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("ano"), anoMax));
            }

            if (generoId != null) {
                predicates.add(cb.equal(root.get("genero").get("id"), generoId));
            }

            if (generoNome != null && !generoNome.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("genero").get("nome")),
                        "%" + generoNome.toLowerCase() + "%"
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return filmeRepository.findAll(specification)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void validarAno(Integer ano) {
        int anoAtual = Year.now().getValue();

        if (ano < 1888) {
            throw new IllegalArgumentException("O ano do filme não pode ser menor que 1888");
        }

        if (ano > anoAtual) {
            throw new IllegalArgumentException("O ano do filme não pode ser maior que " + anoAtual);
        }
    }

    private FilmeResponse toResponse(Filme filme) {
        return new FilmeResponse(
                filme.getId(),
                filme.getTitulo(),
                filme.getDiretor(),
                filme.getAno(),
                filme.getGenero().getId(),
                filme.getGenero().getNome()
        );
    }
}
