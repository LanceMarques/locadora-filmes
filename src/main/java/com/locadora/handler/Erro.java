package com.locadora.handler;

/**
 * Model para mensagem de erro do usuario e desenvolvedor
 * 
 * @author KALEB, Eddie
 */
public class Erro {

	private String mensagemUsuario;

	private String mensagemDesenvolvedor;

	public String getMensagemUsuario() {
		return mensagemUsuario;
	}

	public void setMensagemUsuario(String mensagemUsuario) {
		this.mensagemUsuario = mensagemUsuario;
	}

	public String getMensagemDesenvolvedor() {
		return mensagemDesenvolvedor;
	}

	public void setMensagemDesenvolvedor(String mensagemDesenvolvedor) {
		this.mensagemDesenvolvedor = mensagemDesenvolvedor;
	}

	public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
		
		this.mensagemUsuario = mensagemUsuario;
		this.mensagemDesenvolvedor = mensagemDesenvolvedor;
	}

	public Erro() {

	}

}
