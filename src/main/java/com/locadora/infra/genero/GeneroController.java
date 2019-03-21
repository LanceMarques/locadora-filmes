package com.locadora.infra.genero;

import java.util.List;
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
		List<Genero> generos = generoService.listarTodos();
		return ResponseEntity.status(HttpStatus.OK).body(generos);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Genero> buscarPorId(@PathVariable("id") int id) {
		Genero genero = generoService.buscarPorId(id);
		return ResponseEntity.status(HttpStatus.OK).body(genero);
	}
	
	@PostMapping
	public ResponseEntity<Genero> criar(@Valid @RequestBody Genero genero, HttpServletResponse response) {
		Genero generoSalvo = generoService.criar(genero);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, generoSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(generoSalvo);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Genero> atualizar(@PathVariable int id, @Valid @RequestBody Genero genero){
		Genero generoAtualizado = generoService.atualizar(id, genero);
		return ResponseEntity.status(HttpStatus.OK).body(generoAtualizado);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Genero> excluir(@PathVariable int id) {
		generoService.excluir(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
	
}
