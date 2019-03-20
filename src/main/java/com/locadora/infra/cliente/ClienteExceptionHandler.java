package com.locadora.infra.cliente;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.locadora.handler.Erro;
import com.locadora.infra.cliente.exceptions.CpfJaCadastradoException;
import com.locadora.infra.genero.exceptions.GeneroJaCadastradoException;

@ControllerAdvice
public class ClienteExceptionHandler extends ResponseEntityExceptionHandler{

	@Autowired
	MessageSource messageSource;
	
	@ExceptionHandler({ CpfJaCadastradoException.class })
	public ResponseEntity<Object> handleCpfJaCadastrado(CpfJaCadastradoException ex,
			WebRequest request) {
		String mensagemUsr = messageSource.getMessage("recurso.cpf-ja-cadastrado", null,
				LocaleContextHolder.getLocale());
		String mensagemDev = ex.toString(); 
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
			WebRequest request) {
		String mensagemUsr = messageSource.getMessage("recurso.cpf-invalido", null,
				LocaleContextHolder.getLocale());
		String mensagemDev = ex.toString(); 
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
}
