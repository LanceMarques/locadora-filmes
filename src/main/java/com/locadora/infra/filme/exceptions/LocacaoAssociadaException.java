package com.locadora.infra.filme.exceptions;

/**
 * Model para erro lançado pelo service da entidade Filme quando é solicitada a exclusão de um filme
 * que já está associado à uma locação
 * 
 * @author Luis Lancellote
 */
public class LocacaoAssociadaException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

}
