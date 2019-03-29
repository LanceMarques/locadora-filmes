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
import com.locadora.infra.locacao.exceptions.LocacaoLimiteDeFilmesException;
import com.locadora.infra.locacao.exceptions.LocacaoNaoEncontradaException;

@ControllerAdvice
public class LocacaoExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler({LocacaoNaoEncontradaException.class})
	public ResponseEntity<Object> handleLocacaoNaoEncontrada(LocacaoNaoEncontradaException ex){
		String mensagemUsr = messageSource.getMessage("locacao-nao-encontrada",null, LocaleContextHolder.getLocale());
		String mensagemDev = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsr,mensagemDev));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erros);
	}
	
	@ExceptionHandler({LocacaoLimiteDeFilmesException.class})
	public ResponseEntity<Object> handleLocacaoLimiteDeFilmes(LocacaoLimiteDeFilmesException ex){
		String mensagemUsr = messageSource.getMessage("locacao-limite-de-filmes",null, LocaleContextHolder.getLocale());
		String mensagemDev = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsr,mensagemDev));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
	}
	
}
