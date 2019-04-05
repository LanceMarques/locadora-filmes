package com.locadora.infra.filme.exceptions;

/**
 * Model para erro lançado pelo service da entidade Filme quando a quantidade locada é superior à
 * quantidade de estoque do filme locado
 * 
 * @author Luis Lancellote
 */
public class FilmeEstoqueIndisponivelException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

}
