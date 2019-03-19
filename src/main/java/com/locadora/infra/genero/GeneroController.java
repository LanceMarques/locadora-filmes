package com.locadora.infra.genero;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.locadora.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/generos")
public class GeneroController {
	
	@Autowired
	private GeneroService generoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public ResponseEntity<List<Genero>> listarTodos(){
		return ResponseEntity.ok(generoService.listarTodos());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Genero> buscarPorId(@PathVariable int id) {
		return ResponseEntity.ok(generoService.buscarPorId(id)); 
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Genero> criar(@Valid @RequestBody Genero genero, HttpServletResponse response) {
		Genero generoSalvo = generoService.criar(genero);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, generoSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(generoSalvo);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Genero> atualizar(@PathVariable int id, @Valid @RequestBody Genero genero){
		Genero generoSalvo = generoService.atualizar(id, genero);
		return ResponseEntity.ok(generoSalvo);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable int id) {
		generoService.excluir(id);		
	}
	
}
