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

/**
 * Classe responsavel por mapear as requisições realizadas nas URIs da entidade Genero
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@RestController
@RequestMapping("/generos")
public class GeneroController {

  @Autowired
  private GeneroService generoService;

  @Autowired
  private ApplicationEventPublisher publisher;

  /**
   * Metodo responsavel por fornecer uma lista com todos os {@link Genero generos} cadastrados no
   * sistema
   * 
   * @return {@link ResponseEntity} com todos os {@link Genero generos} cadastrados
   */
  @GetMapping
  public ResponseEntity<List<Genero>> listarTodos() {
    final List<Genero> generos = this.generoService.listarTodos();
    return ResponseEntity.status(HttpStatus.OK).body(generos);
  }

  /**
   * Metodo responsavel por fornecer um {@link Genero genero} que possui o id informado.
   * 
   * @param id ({@link Integer}) Id requisitado na pesquisa
   * @return {@link ResponseEntity} com o genero que possui o id informado.
   * 
   * @since 1.0.0
   */
  @GetMapping("/{id}")
  public ResponseEntity<Genero> buscarPorId(@PathVariable("id") Integer id) {
    final Genero genero = this.generoService.buscarPorId(id);
    return ResponseEntity.status(HttpStatus.OK).body(genero);
  }

  /**
   * Metodo responsavel por cadastrar um genero recebido como parametro
   * 
   * @param genero ({@link Genero}) genero recebido como parametro para cadastro no sistema
   * @return {@link ResponseEntity} com o genero{@link Genero} cadastrado no sistema.
   * 
   * @since 1.0.0
   */
  @PostMapping
  public ResponseEntity<Genero> criar(@Valid @RequestBody Genero genero,
      HttpServletResponse response) {
    final Genero generoSalvo = this.generoService.criar(genero);
    // this.publisher.publishEvent(new RecursoCriadoEvent(this, response, generoSalvo.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(generoSalvo);
  }

  /**
   * Metodo responsavel por atualizar um {@link Genero genero} que possui o id recebido no path com
   * os dados recebidos no corpo da requisicao.
   * 
   * @param id ({@link Integer}) Id do genero a ser atualizado.
   * @param genero {@link Genero} Dados a serem atualizados.
   * 
   * @return {@link ResponseEntity} sem conteudo.
   * 
   * @since 1.0.0
   */
  @PutMapping("/{id}")
  public ResponseEntity<?> atualizar(@PathVariable("id") Integer id,
      @Valid @RequestBody Genero genero) {
    this.generoService.atualizar(id, genero);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  /**
   * Metodo responsavel por excluir um {@link Genero genero} que possui o id informado.
   * 
   * @param id ({@link Integer}) Id do genero a ser excluido.
   * @return {@link ResponseEntity} sem conteudo.
   * 
   * @since 1.0.0
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Genero> excluir(@PathVariable("id") Integer id) {
    this.generoService.excluir(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

}
