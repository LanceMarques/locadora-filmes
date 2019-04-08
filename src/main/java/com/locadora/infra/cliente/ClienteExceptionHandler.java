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

/**
 * Classe responsavel por lidar com as excecoes lancadas pelo service da
 * entidade Cliente
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@ControllerAdvice
public class ClienteExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	/**
	   * Metodo que trata a excecao lancada quando um {@link Cliente cliente} nao e encontrado
	   * 
	   * @param ex {@link ClienteNaoEncontradoException} Excecao
	   * 
	   * @return resposta para o cliente, informando o status NOT_FOUND, alem da mensagem para o
	   *         desenvolvedor do front-end (informacoes pertinentes sobre o erro ocorrido), bem como
	   *         mensagem para o usuario (validacao dos campos, mensagem alto nivel)
	   */
	@ExceptionHandler({ ClienteNaoEncontradoException.class })
	public ResponseEntity<Object> handleClienteNaoEncontrado(ClienteNaoEncontradoException ex) {
		final String mensagemUsr = messageSource.getMessage("cliente-nao-encontrado", null,
				LocaleContextHolder.getLocale());
		final String mensagemDev = ex.toString();
		final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erros);
	}

	/**
     * Metodo que trata a excecao lancada quando um {@link Cliente cliente} a ser cadastrado possui um CPF que ja esta cadastrado no banco.
     * 
     * @param ex {@link CpfJaCadastradoException} Excecao.
     * 
     * @return resposta para o cliente, informando o status BAD_REQUEST, alem da mensagem para o
     *         desenvolvedor do front-end (informacoes pertinentes sobre o erro ocorrido), bem como
     *         mensagem para o usuario (validacao dos campos, mensagem alto nivel)
     */
	@ExceptionHandler({ CpfJaCadastradoException.class })
	public ResponseEntity<Object> handleCpfJaCadastrado(CpfJaCadastradoException ex) {
		final String mensagemUsr = messageSource.getMessage("cpf-ja-cadastrado", null, LocaleContextHolder.getLocale());
		final String mensagemDev = ex.toString();
		final List<Erro> erros = Arrays.asList(new Erro(mensagemUsr, mensagemDev));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
	}

}
