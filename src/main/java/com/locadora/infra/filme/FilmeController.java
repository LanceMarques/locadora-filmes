package com.locadora.infra.filme;

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
@RequestMapping("/filmes")
public class FilmeController {

  @Autowired
  private FilmeService filmeService;

  @Autowired
  private ApplicationEventPublisher publisher;

  @GetMapping
  public ResponseEntity<List<Filme>> listarTodos() {
    final List<Filme> filmes = this.filmeService.listarTodos();
    return ResponseEntity.status(HttpStatus.OK).body(filmes);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Filme> buscarPorId(@PathVariable("id") Integer id) {
    final Filme filme = this.filmeService.buscarPorId(id);
    return ResponseEntity.status(HttpStatus.OK).body(filme);
  }

  @PostMapping
  public ResponseEntity<Filme> criar(@Valid @RequestBody Filme filme,
      HttpServletResponse response) {
    final Filme filmeSalvo = this.filmeService.criar(filme);
    this.publisher.publishEvent(new RecursoCriadoEvent(this, response, filmeSalvo.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(filmeSalvo);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Filme> atualizar(@PathVariable("id") Integer id,
      @Valid @RequestBody Filme filme) {
    final Filme filmeAtualizado = this.filmeService.atualizar(id, filme);
    return ResponseEntity.status(HttpStatus.OK).body(filmeAtualizado);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Filme> excluir(@PathVariable("id") Integer id) {
    this.filmeService.excluir(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}
