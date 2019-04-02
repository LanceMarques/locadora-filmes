package com.locadora.infra.cliente;

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
import com.locadora.infra.cliente.exceptions.ClienteNaoEncontradoException;
import com.locadora.infra.cliente.exceptions.CpfJaCadastradoException;

@ControllerAdvice
public class ClienteExceptionHandler {

  @Autowired
  private MessageSource messageSource;

  @ExceptionHandler({ClienteNaoEncontradoException.class})
  public ResponseEntity<Object> handleClienteNaoEncontrado(ClienteNaoEncontradoException ex) {
    final String mensagemUsr =
        messageSource.getMessage("cliente-nao-encontrado", null, LocaleContextHolder.getLocale());
    final String mensagemDev = ex.toString();
    final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erros);
  }

  @ExceptionHandler({CpfJaCadastradoException.class})
  public ResponseEntity<Object> handleCpfJaCadastrado(CpfJaCadastradoException ex) {
    final String mensagemUsr =
        messageSource.getMessage("cpf-ja-cadastrado", null, LocaleContextHolder.getLocale());
    final String mensagemDev = ex.toString();
    final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
  }

}
