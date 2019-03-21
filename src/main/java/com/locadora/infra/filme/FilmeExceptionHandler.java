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
import org.springframework.web.context.request.WebRequest;

import com.locadora.handler.Erro;
import com.locadora.infra.cliente.exceptions.ClienteNaoEncontradoException;
import com.locadora.infra.filme.exceptions.FilmeJaCadastradoException;
import com.locadora.infra.filme.exceptions.FilmeNaoEncontradoException;

@ControllerAdvice
public class FilmeExceptionHandler {

	@Autowired
	MessageSource messageSource;
	
	@ExceptionHandler({FilmeNaoEncontradoException.class})
	public ResponseEntity<Object> handleFilmeNaoEncontradoException(FilmeNaoEncontradoException ex, WebRequest request){
		String mensagemUsr = messageSource.getMessage("filme.filme-nao-encontrado", null,
				LocaleContextHolder.getLocale());
		String mensagemDev = ex.toString(); 
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erros);
	}
	
	@ExceptionHandler({FilmeJaCadastradoException.class})
	public ResponseEntity<Object> handleFilmeJaCadastradoException(FilmeJaCadastradoException ex, WebRequest request){
		String mensagemUsr = messageSource.getMessage("filme.filme-ja-cadastrado", null,
				LocaleContextHolder.getLocale());
		String mensagemDev = ex.toString(); 
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erros);
	}
	
}