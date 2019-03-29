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
		int filmeId;
		int qtdLocada;
		Filme filmeSalvo;
		LocacaoTemFilme filmeLocado;
		
		List<LocacaoTemFilme> filmes = new ArrayList<>();
		
		for (LocacaoTemFilme locacaoTemFilme : filmesLocados) {
			filmeId = locacaoTemFilme.getFilme().getId();
			qtdLocada = locacaoTemFilme.getQuantidadeLocada();
			
			filmeSalvo = filmeService.buscarPorIdComEstoqueDisponivel(filmeId, qtdLocada);
			
			filmeLocado = new LocacaoTemFilme(locacao, filmeSalvo);
			filmeLocado.setQuantidadeLocada(locacaoTemFilme.getQuantidadeLocada());
			calculaValorTotalDiaria(filmeLocado);
			filmes.add(filmeLocado);
		}		
		return filmes;
	}
	
	public int contarFilmesNasLocacoes(List<Locacao> locacoes) {
		int qtdFilmes=0;
		for (Locacao locacao: locacoes) {
			qtdFilmes+=contarFilmes(locacao.getFilmes());
		}
		return qtdFilmes;
	}
	
	public int contarFilmes(List<LocacaoTemFilme> filmesLocados) {
		int qtdFilmes=0;
		for (LocacaoTemFilme filme : filmesLocados) {
			qtdFilmes+=filme.getQuantidadeLocada();
		}
		return qtdFilmes;
	}
	
	public void calculaValorTotalDiaria(LocacaoTemFilme locacaoTemFilme) {
		double valorTotalDiaria;
		
		final double valorDiaria = locacaoTemFilme.getFilme().getValorDiaria();
		final int qtdLocada = locacaoTemFilme.getQuantidadeLocada();
		valorTotalDiaria = valorDiaria*qtdLocada;
		
		locacaoTemFilme.setValorTotalDaDiaria(valorTotalDiaria);
	}
	
	public void salvaTodas(List<LocacaoTemFilme> filmesLocados) {
		locacaoTemFilmeRepository.saveAll(filmesLocados);
	}
	
}
