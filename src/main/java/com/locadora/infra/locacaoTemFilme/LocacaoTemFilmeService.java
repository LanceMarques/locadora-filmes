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
		List<Filme> filmesDasLocacoes = new ArrayList<>();
		
		for (LocacaoTemFilme locacaoTemFilme : filmesLocados) {
			filmeId = locacaoTemFilme.getFilme().getId();
			qtdLocada = locacaoTemFilme.getQuantidadeLocada();
						
			filmeSalvo = this.filmeService.buscarReduzirEstoque(filmeId, qtdLocada);
			
			filmeLocado = new LocacaoTemFilme(locacao, filmeSalvo, qtdLocada);
			this.calculaValorTotalDiaria(filmeLocado);
			
			filmesDasLocacoes.add(filmeSalvo);
			filmes.add(filmeLocado);
		}
		this.filmeService.salvarTodos(filmesDasLocacoes);
		return filmes;
	}
		
	public void devolverAoEstoque(List<LocacaoTemFilme> filmesLocados) {
		Filme filmeDevolvido;
		int filmeId;
		List<Filme> filmesDevolvidos = new ArrayList<>();
		
		for (LocacaoTemFilme filmeLocado : filmesLocados) {
			filmeId = filmeLocado.getFilme().getId();
			filmeDevolvido = this.filmeService.buscarPorId(filmeId);
			filmesDevolvidos.add(filmeDevolvido);
		}
		this.filmeService.salvarTodos(filmesDevolvidos);
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
		for (LocacaoTemFilme filmeLocado: filmesLocados) {
			qtdFilmes+=filmeLocado.getQuantidadeLocada();
		}
		return qtdFilmes;
	}
	
	public void calculaValorTotalDiaria(LocacaoTemFilme locacaoTemFilme) {
		final double valorDiaria = locacaoTemFilme.getFilme().getValorDiaria();
		final int qtdLocada = locacaoTemFilme.getQuantidadeLocada();
		final double valorTotalDiaria = valorDiaria*qtdLocada;
		
		locacaoTemFilme.setValorTotalDaDiaria(valorTotalDiaria);
	}
	
}
