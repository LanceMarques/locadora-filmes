package com.locadora.infra.genero;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.locadora.handler.Erro;
import com.locadora.infra.genero.exceptions.FilmeAssociadoException;
import com.locadora.infra.genero.exceptions.GeneroJaCadastradoException;
import com.locadora.infra.genero.exceptions.GeneroNaoEncontradoException;

/**
 * Classe responsavel por lidar com as excecoes lancadas pelo service da entidade Genero
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@ControllerAdvice
public class GeneroExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired
  private MessageSource messageSource;

  /**
   * Metodo que trata a excecao lancada quando um {@link Genero genero} nao e encontrado
   * 
   * @param ex {@link GeneroNaoEncontradoException} Excecao
   * 
   * @return resposta para o cliente, informando o status NOT_FOUND, alem da mensagem para o
   *         desenvolvedor do front-end (informacoes pertinentes sobre o erro ocorrido), bem como
   *         mensagem para o usuario (validacao dos campos, mensagem alto nivel)
   */
  @ExceptionHandler({GeneroNaoEncontradoException.class})
  public ResponseEntity<Object> handleGeneroNaoEncontradoException(
      GeneroNaoEncontradoException ex) {
    final String mensagemUsr =
        messageSource.getMessage("genero-nao-encontrado", null, LocaleContextHolder.getLocale());
    final String mensagemDev = ex.toString();
    final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erros);
  }

  /**
   * Metodo que trata a excecao lancada quando um {@link Genero genero} a ser cadastrado ja existe
   * no banco.
   * 
   * @param ex {@link GeneroJaCadastradoException} Excecao
   * 
   * @return resposta para o cliente, informando o status BAD_REQUEST, alem da mensagem para o
   *         desenvolvedor do front-end (informacoes pertinentes sobre o erro ocorrido), bem como
   *         mensagem para o usuario (validacao dos campos, mensagem alto nivel)
   */
  @ExceptionHandler({GeneroJaCadastradoException.class})
  public ResponseEntity<Object> handleGeneroJaCadastradoException(GeneroJaCadastradoException ex) {
    final String mensagemUsr =
        messageSource.getMessage("genero-ja-cadastrado", null, LocaleContextHolder.getLocale());
    final String mensagemDev = ex.toString();
    final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
  }

  /**
   * Metodo que trata a excecao lancada quando um {@link Genero genero} a ser excluido ainda esta
   * associado a um filme.
   * 
   * @param ex {@link FilmeAssociadoException} Excecao
   * 
   * @return resposta para o cliente, informando o status BAD_REQUEST, alem da mensagem para o
   *         desenvolvedor do front-end (informacoes pertinentes sobre o erro ocorrido), bem como
   *         mensagem para o usuario (validacao dos campos, mensagem alto nivel)
   */
  @ExceptionHandler({FilmeAssociadoException.class})
  public ResponseEntity<Object> handleFilmeAssociadoException(FilmeAssociadoException ex) {
    final String mensagemUsr =
        messageSource.getMessage("genero.filme-associado", null, LocaleContextHolder.getLocale());
    final String mensagemDev = ex.toString();
    final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
  }
}
