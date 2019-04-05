package com.locadora.infra.locacao;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.locadora.infra.filme.Filme;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilme;

/**
 * Classe responsavel por mapear as requisições realizadas nas URIs da entidade Locacao
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@RestController
@RequestMapping("/locacoes")
public class LocacaoController {

  @Autowired
  private LocacaoService locacaoService;

  /**
   * Metodo responsavel por fornecer uma lista com todas as {@link Locacao locacoes} cadastradas no
   * sistema
   * 
   * @return {@link ResponseEntity} com todas as {@link Locacao locacoes} cadastradas
   */
  @GetMapping
  public ResponseEntity<List<Locacao>> listarTodas(LocacaoFilter filter) {
    List<Locacao> locacoes = this.locacaoService.pesquisar(filter);
    return ResponseEntity.status(HttpStatus.OK).body(locacoes);
  }

  /**
   * Metodo responsavel por fornecer uma {@link Locacao locacao} que possui o id informado.
   * 
   * @param id ({@link Integer}) Id requisitado na pesquisa
   * @return {@link ResponseEntity} com o filme que possui o id informado.
   * 
   * @since 1.0.0
   */
  @GetMapping("/{id}")
  public ResponseEntity<Locacao> buscarPorId(@PathVariable Integer id) {
    Locacao locacaoSalva = this.locacaoService.buscarPorId(id);
    return ResponseEntity.status(HttpStatus.OK).body(locacaoSalva);
  }

  /**
   * Metodo responsavel por fornecer uma lista de filmes{@link Filme} de uma locacao{@link Locacao}
   * que possua o id informado
   * 
   * @param id ({@link Integer}) Id requisitado na pesquisa
   * @return {@link ResponseEntity} com a lista de filmes da locacao solicitada
   * 
   * @since 1.0.0
   */
  @GetMapping("/{id}/filmes")
  public ResponseEntity<List<LocacaoTemFilme>> listarFilmesPorId(@PathVariable Integer id) {
    List<LocacaoTemFilme> filmesDaLocacao = this.locacaoService.listarFilmes(id);
    return ResponseEntity.status(HttpStatus.OK).body(filmesDaLocacao);
  }

  /**
   * Metodo responsavel por fornecer uma lista de locacoes{@link Locacao} em aberto de um cliente
   * que possua o CPF recebido como parametro
   * 
   * @param cpf ({@link String}) CPF requisitado na pesquisa
   * @return {@link ResponseEntity} com todas as {@link Locacao locacoes} cadastradas
   * 
   * @since 1.0.0
   */
  @GetMapping("/cliente/cpf/{cpf}")
  public ResponseEntity<List<Locacao>> listarPorCliente(@PathVariable String cpf) {
    List<Locacao> locacoes = this.locacaoService.listarPorCPF(cpf);
    return ResponseEntity.status(HttpStatus.OK).body(locacoes);
  }

  /**
   * Metodo responsavel por cadastrar uma locacao recebida como parametro
   * 
   * @param locacao ({@link Locacao}) locacao recebida como parametro para cadastro no sistema
   * @return {@link ResponseEntity} com a locacao{@link Locacao} cadastrada no sistema.
   * 
   * @since 1.0.0
   */
  @PostMapping
  public ResponseEntity<Locacao> criar(@Valid @RequestBody Locacao locacao) {
    Locacao locacaoSalva = locacaoService.criar(locacao);
    return ResponseEntity.status(HttpStatus.CREATED).body(locacaoSalva);
  }

  /**
   * Metodo responsavel por atualizar uma {@link Locacao locacao} que possui o id recebido no path
   * com os dados recebidos no corpo da requisicao.
   * 
   * @param id ({@link Integer}) Id da locacao a ser atualizada.
   * @param locacao {@link Locacao} Dados a serem atualizados.
   * 
   * @return {@link ResponseEntity} sem conteudo.
   * 
   * @since 1.0.0
   */
  @PutMapping("/{id}")
  public ResponseEntity<Locacao> atualizar(@PathVariable Integer id, @RequestBody Locacao locacao) {
    Locacao locacaoAtualizada = this.locacaoService.atualizar(id, locacao);
    return ResponseEntity.status(HttpStatus.OK).body(locacaoAtualizada);
  }

  /**
   * Metodo responsavel por efetivar a devolução de uma locacao que possui o id recebido como parametro 
   * 
   * @param id ({@link Integer}) Id da locacao a ser devolvida.
   * 
   * @return {@link ResponseEntity} sem conteudo.
   * 
   * @since 1.0.0
   */
  @PutMapping("/{id}/devolucao")
  public ResponseEntity<?> devolverLocacao(@PathVariable Integer id) {
    this.locacaoService.devolverLocacao(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }


  /**
   * Metodo responsavel por excluir uma {@link Locacao locacao} que possui o id informado.
   * 
   * @param id ({@link Integer}) Id da locacao a ser excluido. 
   * @return {@link ResponseEntity} sem conteudo.
   * 
   * @since 1.0.0
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> excluir(@PathVariable Integer id) {
    this.locacaoService.excluir(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}