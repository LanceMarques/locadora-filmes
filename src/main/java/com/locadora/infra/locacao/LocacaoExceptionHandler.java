package com.locadora.infra.locacao;

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
import com.locadora.infra.cliente.Cliente;
import com.locadora.infra.locacao.exceptions.LocacaoLimiteDeFilmesException;
import com.locadora.infra.locacao.exceptions.LocacaoNaoEncontradaException;

/**
 * Classe responsavel por lidar com as excecoes lancadas pelo service da entidade Locacao
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@ControllerAdvice
public class LocacaoExceptionHandler {

  @Autowired
  private MessageSource messageSource;

  /**
   * Metodo que trata a excecao lancada quando uma {@link Locacao locacao} nao e encontrada.
   * 
   * @param ex {@link LocacaoNaoEncontradaException} Excecao.
   * 
   * @return resposta para o cliente, informando o status NOT_FOUND, alem da mensagem para o
   *         desenvolvedor do front-end (informacoes pertinentes sobre o erro ocorrido), bem como
   *         mensagem para o usuario (validacao dos campos, mensagem alto nivel)
   */
  @ExceptionHandler({LocacaoNaoEncontradaException.class})
  public ResponseEntity<Object> handleLocacaoNaoEncontrada(LocacaoNaoEncontradaException ex) {
    final String mensagemUsr =
        messageSource.getMessage("locacao-nao-encontrada", null, LocaleContextHolder.getLocale());
    final String mensagemDev = ex.toString();
    final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erros);
  }

  /**
   * Metodo que trata a excecao lancada quando um {@link Cliente cliente} ja atingiu o limite de filmes permitido.
   * 
   * @param ex {@link LocacaoLimiteDeFilmesException} Excecao.
   * 
   * @return resposta para o cliente, informando o status BAD_REQUEST, alem da mensagem para o
   *         desenvolvedor do front-end (informacoes pertinentes sobre o erro ocorrido), bem como
   *         mensagem para o usuario (validacao dos campos, mensagem alto nivel)
   */
  @ExceptionHandler({LocacaoLimiteDeFilmesException.class})
  public ResponseEntity<Object> handleLocacaoLimiteDeFilmes(LocacaoLimiteDeFilmesException ex) {
    final String mensagemUsr =
        messageSource.getMessage("locacao-limite-de-filmes", null, LocaleContextHolder.getLocale());
    final String mensagemDev = ex.toString();
    final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
  }

}
