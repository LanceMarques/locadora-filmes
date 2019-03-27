package com.locadora.infra.locacaoTemFilme;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.locadora.infra.filme.Filme;
import com.locadora.infra.filme.FilmeService;
import com.locadora.infra.locacao.Locacao;

@Service
public class LocacaoTemFilmeService {

	@Autowired
	private LocacaoTemFilmeRepository locacaoTemFilmeRepository;
	
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
	
	public List<LocacaoTemFilme> associarFilmes(List<LocacaoTemFilme> filmesLocados, Locacao locacao) {
		Filme filmeSalvo;
		LocacaoTemFilme filmeLocado;
		List<LocacaoTemFilme> filmes = new ArrayList<>();
		int filmeId;
		for (LocacaoTemFilme locacaoTemFilme : filmesLocados) {
			filmeLocado = locacaoTemFilme;
			filmeId = locacaoTemFilme.getFilme().getId();
			filmeSalvo = filmeService.buscarPorId(filmeId);
			filmeLocado =new LocacaoTemFilme(locacao, filmeSalvo);
			filmeLocado.setValorTotalDaDiaria(filmeLocado.getQuantidadeLocada()*filmeSalvo.getValorDiaria());
			filmes.add(filmeLocado);
		}		
		return filmes;
	}
	
	public void salvaTodas(List<LocacaoTemFilme> filmesLocados) {
		locacaoTemFilmeRepository.saveAll(filmesLocados);
	}
	
}
