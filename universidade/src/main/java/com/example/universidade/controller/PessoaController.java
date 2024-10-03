package com.example.universidade.controller;

import com.example.universidade.dto.PessoaDTO;
import com.example.universidade.model.Pessoa;
import com.example.universidade.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping
    public List<PessoaDTO> listarPessoas() {
        return pessoaRepository.findAll().stream()
                .map(pessoa -> new PessoaDTO(pessoa.getNome(), pessoa.getCpf(), pessoa.getIdade()))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<PessoaDTO> criarPessoa(@RequestBody PessoaDTO pessoaDTO) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(pessoaDTO.getNome());
        pessoa.setCpf(pessoaDTO.getCpf());
        pessoa.setIdade(pessoaDTO.getIdade());
        
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);
        
        return new ResponseEntity<>(new PessoaDTO(pessoaSalva.getNome(), pessoaSalva.getCpf(), pessoaSalva.getIdade()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaDTO> obterPessoa(@PathVariable Long id) {
        return pessoaRepository.findById(id)
                .map(pessoa -> new PessoaDTO(pessoa.getNome(), pessoa.getCpf(), pessoa.getIdade()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable Long id) {
        if (pessoaRepository.existsById(id)) {
            pessoaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

