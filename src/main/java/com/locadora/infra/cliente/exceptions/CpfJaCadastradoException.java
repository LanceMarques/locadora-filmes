package com.locadora.infra.cliente.exceptions;

/**
 * Model para erro lançado pelo service da entidade Cliente ao tentar cadastrar um cliente que já
 * possui CPF cadastrado no sistema
 * 
 * @author Luis Lancellote
 */
public class CpfJaCadastradoException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

}
