package com.locadora.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.NonUniqueResultException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Classe responsavel por lidar com as excecoes genericas lancadas pelo Sistema
 * 
 * @version 1.0.0 Fevereiro/2019
 * @author LUCENA, Caio
 * @since 1.0.0
 */
@ControllerAdvice
public class LocadoraExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Classe que faz a internaciolizacao se conectando com o messages.properties.
   */
  @Autowired
  private MessageSource messageSource;

  /**
   * Metodo que trata a excecao lancada quando o cliente envia uma requisicao contendo um body
   * malformatado
   * 
   * @param HttpMessageNotReadableException mensagem lancada pela excecao
   * @param HttpHeaders cabecalho da requisicao
   * @param HttpStatus status que deve ser retornado para o erro lancado
   * @param WebRequest requisicao
   * @return resposta para o cliente, informando o status BAD_REQUEST, alem da mensagem para o
   *         desenvolvedor do front-end (informacoes pertinentes sobre o erro ocorrido), bem como
   *         mensagem para o usuario (validacao dos campos, mensagem alto nivel)
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    String mensagemUsr =
        messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
    String mensagemDev = ex.getCause().toString();

    List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Metodo que trata a excecao lancada quando o cliente envia uma requisicao contendo campos
   * obrigatorios vazios ou com tamanho minimo/maximo fora do permitido
   * 
   * @param ex MethodArgumentNotValidException mensagem lancada pela excecao
   * @param headers HttpHeaders cabecalho da requisicao
   * @param status HttpStatus status que deve ser retornado para o erro lancado
   * @param request WebRequest requisicao
   * @return resposta para o cliente, informando o status BAD_REQUEST, alem da mensagem para o
   *         desenvolvedor do front-end (informacoes pertinentes sobre o erro ocorrido), bem como
   *         mensagem para o usuario (validacao dos campos, mensagem alto nivel)
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<Erro> erros = criarListaDeErros(ex.getBindingResult());
    return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Metodo que trata a excecao lancada quando o cliente envia uma requisicao que viola as
   * integridades dos dados no banco de dados. Ex: Unique keys
   * 
   * @param ex DataIntegrityViolationException mensagem lancada pela excecao
   * @param request WebRequest requisicao
   * @return resposta para o cliente, informando o status BAD_REQUEST, alem da mensagem para o
   *         desenvolvedor do front-end (informacoes pertinentes sobre o erro ocorrido), bem como
   *         mensagem para o usuario (validacao dos campos, mensagem alto nivel)
   */
  @ExceptionHandler({DataIntegrityViolationException.class})
  public ResponseEntity<Object> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex, WebRequest request) {
    String mensagemUsr = messageSource.getMessage("recurso.operacao-nao-permitida", null,
        LocaleContextHolder.getLocale());
    String mensagemDev = ExceptionUtils.getRootCauseMessage(ex);
    List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler({IllegalArgumentException.class})
  public ResponseEntity<Object> handleIllegalArgumentException(
      IllegalArgumentException ex, WebRequest request) {
    String mensagemUsr = messageSource.getMessage("recurso.dados-incompletos", null,
        LocaleContextHolder.getLocale());
    String mensagemDev = ExceptionUtils.getRootCauseMessage(ex);
    List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler({NonUniqueResultException.class})
  public ResponseEntity<Object> handleNonUniqueResultException(NonUniqueResultException ex,
      WebRequest request) {
    String mensagemUsr = messageSource.getMessage("genero.cadastro-replicado", null,
        LocaleContextHolder.getLocale());
    String mensagemDev = ExceptionUtils.getRootCauseMessage(ex);
    List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Metodo que cria uma lista de erros. Usado quando ha mais que uma mensagem de erro a ser
   * mostrada ao usuario e ao desenvolvedor.
   * 
   * @param BindingResult - objeto que contem os campos onde ocorreram os erros
   * @return Lista de erros criada de acordo com o bindingResult
   */
  private List<Erro> criarListaDeErros(BindingResult bindingResult) {
    List<Erro> erros = new ArrayList<>();

    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      String mensagemUsr = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
      String mensagemDev = fieldError.toString();
      erros.add(new Erro(mensagemUsr, mensagemDev));
    }
    return erros;
  }

}
