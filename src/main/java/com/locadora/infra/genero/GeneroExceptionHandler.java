package com.locadora.infra.genero;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.locadora.handler.Erro;
import com.locadora.infra.genero.exceptions.GeneroJaCadastradoException;
import com.locadora.infra.genero.exceptions.GeneroNaoEncontradoException;

@ControllerAdvice
public class GeneroExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Autowired
	MessageSource messageSource;
	
	@ExceptionHandler({ GeneroNaoEncontradoException.class })
	public ResponseEntity<Object> handleGeneroNaoEncontradoException(GeneroNaoEncontradoException ex,
			WebRequest request) {
		String mensagemUsr = messageSource.getMessage("recurso.genero-nao-encontrado", null,
				LocaleContextHolder.getLocale());
		String mensagemDev = ex.toString(); 
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({ GeneroJaCadastradoException.class })
	public ResponseEntity<Object> handleGeneroJaCadastrado(GeneroJaCadastradoException ex,
			WebRequest request) {
		String mensagemUsr = messageSource.getMessage("recurso.genero-ja-cadastrado", null,
				LocaleContextHolder.getLocale());
		String mensagemDev = ex.toString(); 
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

}
