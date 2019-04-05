package com.locadora.infra.locacao.exceptions;

/**
 * Model para erro lançado pelo service da entidade Locacao quando o cliente tenta locar um número
 * de filmes acima do limite permitido
 * 
 * @author Luis Lancellote
 */
public class LocacaoLimiteDeFilmesException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

}
