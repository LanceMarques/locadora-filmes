package com.locadora.infra.locacaoTemFilme;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.locadora.infra.filme.Filme;
import com.locadora.infra.filme.FilmeService;

@Service
public class LocacaoTemFilmeService {

	@Autowired
	private FilmeService filmeService;
	
	public void calculaTotalDiaria(List<LocacaoTemFilme> filmes) {
		Filme filmeSalvo;
		double valorTotalDiaria;
		for (LocacaoTemFilme filmeLocado : filmes) {
			int quantidade = filmeLocado.getQuantidadeLocada();
			int filmeId = filmeLocado.getFilme().getId();
			filmeSalvo = filmeService.buscarPorId(filmeId);
			
			valorTotalDiaria = quantidade * filmeSalvo.getValorDiaria();
			
			filmeLocado.setValorTotalDaDiaria(valorTotalDiaria);
		}
	}
	
}
