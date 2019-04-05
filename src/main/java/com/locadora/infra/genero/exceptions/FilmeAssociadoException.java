package com.locadora.infra.genero.exceptions;

/**
 * Model para erro lançado pelo service da entidade Genero quando é solicitada a exclusão de um
 * gênero que ainda está associado à um filme
 * 
 * @author Luis Lancellote
 */
public class FilmeAssociadoException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

}
