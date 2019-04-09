package com.locadora.utils;

import java.sql.Date;

/**
 * Classe responsavel disponibilizar metodos utilitarios para datas em geral.
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
public class DataUtils {

	/**
	 * Metodo responsavel por disponibilizar a data atual.
	 * 
	 * @return {@link Date Data} atual.
	 */
	public static Date gerarDataAtual() {
		return new Date(System.currentTimeMillis());
	}

}
