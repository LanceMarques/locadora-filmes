package com.locadora.infra.filme;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.locadora.handler.Erro;
import com.locadora.infra.filme.exceptions.FilmeEstoqueIndisponivelException;
import com.locadora.infra.filme.exceptions.FilmeNaoEncontradoException;
import com.locadora.infra.filme.exceptions.LocacaoAssociadaException;

/**
 * Classe responsavel por lidar com as excecoes lancadas pelo service da entidade Filme
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@ControllerAdvice
public class FilmeExceptionHandler {

  @Autowired
  private MessageSource messageSource;

  /**
   * Metodo que trata a excecao lancada quando um {@link Filme filme} nao e encontrado.
   * 
   * @param ex {@link FilmeNaoEncontradoException} Excecao
   * 
   * @return resposta para o cliente, informando o status NOT_FOUND, alem da mensagem para o
   *         desenvolvedor do front-end (informacoes pertinentes sobre o erro ocorrido), bem como
   *         mensagem para o usuario (validacao dos campos, mensagem alto nivel)
   */
  @ExceptionHandler({FilmeNaoEncontradoException.class})
  public ResponseEntity<Object> handleFilmeNaoEncontradoException(FilmeNaoEncontradoException ex) {
    final String mensagemUsr =
        messageSource.getMessage("filme-nao-encontrado", null, LocaleContextHolder.getLocale());
    final String mensagemDev = ex.toString();
    final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erros);
  }

  /**
   * Metodo que trata a excecao lancada quando um {@link Filme filme} nao possui estoque disponivel
   * para locacao.
   * 
   * @param ex {@link FilmeEstoqueIndisponivelException} Excecao
   * 
   * @return resposta para o cliente, informando o status BAD_REQUEST, alem da mensagem para o
   *         desenvolvedor do front-end (informacoes pertinentes sobre o erro ocorrido), bem como
   *         mensagem para o usuario (validacao dos campos, mensagem alto nivel)
   */
  @ExceptionHandler({FilmeEstoqueIndisponivelException.class})
  public ResponseEntity<Object> handleFilmeEstoqueIndisponivelException(
      FilmeEstoqueIndisponivelException ex) {
    final String mensagemUsr = messageSource.getMessage("filme-estoque-indisponivel", null,
        LocaleContextHolder.getLocale());
    final String mensagemDev = ex.toString();
    final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
  }

  /**
   * Metodo que trata a excecao lancada quando um {@link Filme filme} a ser excluido ainda esta
   * associado a uma locacao.
   * 
   * @param ex {@link LocacaoAssociadaException} Excecao
   * 
   * @return resposta para o cliente, informando o status BAD_REQUEST), alem da mensagem para o
   *         desenvolvedor do front-end (informacoes pertinentes sobre o erro ocorrido), bem como
   *         mensagem para o usuario (validacao dos campos, mensagem alto nivel)
   */
  @ExceptionHandler({LocacaoAssociadaException.class})
  public ResponseEntity<Object> handleLocacaoAssociadaException(LocacaoAssociadaException ex) {
    final String mensagemUsr =
        messageSource.getMessage("filme-locacao-associada", null, LocaleContextHolder.getLocale());
    final String mensagemDev = ex.toString();
    final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
  }

}
