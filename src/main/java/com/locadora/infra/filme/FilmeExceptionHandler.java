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
import com.locadora.infra.filme.exceptions.FilmeJaCadastradoException;
import com.locadora.infra.filme.exceptions.FilmeNaoEncontradoException;
import com.locadora.infra.filme.exceptions.LocacaoAssociadaException;

@ControllerAdvice
public class FilmeExceptionHandler {

  @Autowired
  private MessageSource messageSource;

  @ExceptionHandler({FilmeNaoEncontradoException.class})
  public ResponseEntity<Object> handleFilmeNaoEncontradoException(FilmeNaoEncontradoException ex) {
    final String mensagemUsr =
        messageSource.getMessage("filme-nao-encontrado", null, LocaleContextHolder.getLocale());
    final String mensagemDev = ex.toString();
    final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erros);
  }

  @ExceptionHandler({FilmeJaCadastradoException.class})
  public ResponseEntity<Object> handleFilmeJaCadastradoException(FilmeJaCadastradoException ex) {
    final String mensagemUsr =
        messageSource.getMessage("filme-ja-cadastrado", null, LocaleContextHolder.getLocale());
    final String mensagemDev = ex.toString();
    final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erros);
  }

  @ExceptionHandler({FilmeEstoqueIndisponivelException.class})
  public ResponseEntity<Object> handleFilmeEstoqueIndisponivelException(
      FilmeEstoqueIndisponivelException ex) {
    final String mensagemUsr = messageSource.getMessage("filme-estoque-indisponivel", null,
        LocaleContextHolder.getLocale());
    final String mensagemDev = ex.toString();
    final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erros);
  }

  @ExceptionHandler({LocacaoAssociadaException.class})
  public ResponseEntity<Object> handleLocacaoAssociadaException(
      LocacaoAssociadaException ex) {
    final String mensagemUsr = messageSource.getMessage("filme-locacao-associada", null,
        LocaleContextHolder.getLocale());
    final String mensagemDev = ex.toString();
    final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erros);
  }
  
}