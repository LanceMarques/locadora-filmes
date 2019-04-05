package com.locadora.infra.genero.exceptions;

/**
 * Model para erro lançado pelo service da entidade Genero ao tentar cadastrar um genero que possui
 * o mesmo nome de outro genero já cadastrado
 * 
 * @author Luis Lancellote
 */
public class GeneroJaCadastradoException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

}
