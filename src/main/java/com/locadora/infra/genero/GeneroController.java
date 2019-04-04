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
  public ResponseEntity<List<Genero>> listarTodos() {
    final List<Genero> generos = this.generoService.listarTodos();
    return ResponseEntity.status(HttpStatus.OK).body(generos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Genero> buscarPorId(@PathVariable("id") Integer id) {
    final Genero genero = this.generoService.buscarPorId(id);
    return ResponseEntity.status(HttpStatus.OK).body(genero);
  }

  @PostMapping
  public ResponseEntity<Genero> criar(@Valid @RequestBody Genero genero,
      HttpServletResponse response) {
    final Genero generoSalvo = this.generoService.criar(genero);
    this.publisher.publishEvent(new RecursoCriadoEvent(this, response, generoSalvo.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(generoSalvo);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> atualizar(@PathVariable("id") Integer id,
      @Valid @RequestBody Genero genero) {
    this.generoService.atualizar(id, genero);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Genero> excluir(@PathVariable("id") Integer id) {
    this.generoService.excluir(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

}
