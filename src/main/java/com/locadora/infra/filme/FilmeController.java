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

/**
 * Classe responsavel por mapear as requisições realizadas nas URIs da entidade Filme 
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@RestController
@RequestMapping("/filmes")
public class FilmeController {

  @Autowired
  private FilmeService filmeService;

  @Autowired
  private ApplicationEventPublisher publisher;

  /**
   * Metodo responsavel por fornecer uma lista com todos os {@link Filme filmes} cadastrados no sistema
   * 
   * @return {@link ResponseEntity} com todos os {@link Filme filmes} cadastrados
   */
  @GetMapping
  public ResponseEntity<List<Filme>> listarTodos() {
    final List<Filme> filmes = this.filmeService.listarTodos();
    return ResponseEntity.status(HttpStatus.OK).body(filmes);
  }

  /**
   * Metodo responsavel por fornecer um {@link Filme filme} que possui o id informado.
   * 
   * @param id ({@link Integer}) Id requisitado na pesquisa
   * @return {@link ResponseEntity} com o filme que possui o id informado.
   * 
   * @since 1.0.0
   */
  @GetMapping("/{id}")
  public ResponseEntity<Filme> buscarPorId(@PathVariable("id") Integer id) {
    final Filme filme = this.filmeService.buscarPorId(id);
    return ResponseEntity.status(HttpStatus.OK).body(filme);
  }

  /**
   * Metodo responsavel por cadastrar um filme recebido como parametro
   * 
   * @param filme ({@link Filme}) filme recebido como parametro para cadastro no sistema
   * @return {@link ResponseEntity} com o filme{@link Filme} cadastrado no sistema.
   * 
   * @since 1.0.0
   */
  @PostMapping
  public ResponseEntity<Filme> criar(@Valid @RequestBody Filme filme,
      HttpServletResponse response) {
    final Filme filmeSalvo = this.filmeService.criar(filme);
    this.publisher.publishEvent(new RecursoCriadoEvent(this, response, filmeSalvo.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(filmeSalvo);
  }

  /**
   * Metodo responsavel por atualizar um {@link Filme filme} que possui o id recebido no path
   * com os dados recebidos no corpo da requisicao.
   * 
   * @param id ({@link Integer}) Id do filme a ser atualizado.
   * @param filme {@link Filme} Dados a serem atualizados.
   * 
   * @return {@link ResponseEntity} sem conteudo.
   * 
   * @since 1.0.0
   */
  @PutMapping("/{id}")
  public ResponseEntity<?> atualizar(@PathVariable("id") Integer id,
      @Valid @RequestBody Filme filme) {
    this.filmeService.atualizar(id, filme);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  /**
   * Metodo responsavel por excluir um {@link Filme filme} que possui o id informado.
   * 
   * @param id ({@link Integer}) Id do filme a ser excluido. 
   * @return {@link ResponseEntity} sem conteudo.
   * 
   * @since 1.0.0
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Filme> excluir(@PathVariable("id") Integer id) {
    this.filmeService.excluir(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}