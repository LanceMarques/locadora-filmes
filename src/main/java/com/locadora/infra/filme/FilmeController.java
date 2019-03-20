package com.locadora.infra.filme;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/filmes")
public class FilmeController {
	
	@Autowired
	private FilmeService filmeService;
	
	@GetMapping
	public ResponseEntity<List<Filme>> listarTodos(){
		List<Filme> filmes = filmeService.listarTodos();
		return ResponseEntity.status(HttpStatus.OK).body(filmes);
	}
	@PostMapping
	public ResponseEntity<Filme> criar(@Valid @RequestBody Filme filme){
		Filme filmeSalvo = filmeService.criar(filme);
		return ResponseEntity.status(HttpStatus.CREATED).body(filmeSalvo);
	}
	
}
