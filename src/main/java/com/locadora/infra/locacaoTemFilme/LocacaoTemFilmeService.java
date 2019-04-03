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

  public List<LocacaoTemFilme>listarPorFilme(Filme filme){
    return this.locacaoTemFilmeRepository.findByFilme(filme);
  }
  
  public void calculaTotalDiaria(List<LocacaoTemFilme> filmes) {
    Filme filmeSalvo;
    Double valorTotalDiaria;
    for (LocacaoTemFilme filmeLocado : filmes) {
      Integer quantidade = filmeLocado.getQuantidadeLocada();
      Integer filmeId = filmeLocado.getFilme().getId();
      filmeSalvo = filmeService.buscarPorId(filmeId);

      valorTotalDiaria = quantidade * filmeSalvo.getValorDiaria();

      filmeLocado.setValorTotalDaDiaria(valorTotalDiaria);
    }
  }

  public List<LocacaoTemFilme> associarFilmes(List<LocacaoTemFilme> filmesLocados,
      Locacao locacao) {
    Integer filmeId;
    Integer qtdLocada;
    Filme filmeSalvo;
    LocacaoTemFilme filmeLocado;

    List<LocacaoTemFilme> filmes = new ArrayList<>();
    List<Filme> filmesDasLocacoes = new ArrayList<>();

    for (LocacaoTemFilme locacaoTemFilme : filmesLocados) {
      filmeId = locacaoTemFilme.getFilme().getId();
      qtdLocada = locacaoTemFilme.getQuantidadeLocada();

      filmeSalvo = this.filmeService.buscarReduzirEstoque(filmeId, qtdLocada);

      filmeLocado = new LocacaoTemFilme(locacao, filmeSalvo, qtdLocada);
      filmeLocado.setValorTotalDaDiaria(this.calcularValorTotalDiaria(filmeLocado));

      filmesDasLocacoes.add(filmeSalvo);
      filmes.add(filmeLocado);
    }
    this.filmeService.salvarTodos(filmesDasLocacoes);
    return filmes;
  }

  public void devolverAoEstoque(List<LocacaoTemFilme> filmesLocados) {
    Filme filmeDevolvido;
    Integer filmeId;
    Integer qtdDevolvida;
    List<Filme> filmesDevolvidos = new ArrayList<>();

    for (LocacaoTemFilme filmeLocado : filmesLocados) {
      System.out.println("ESTOQUE 1 ="+filmeLocado.getFilme().getQuantidadeEstoque());
      filmeId = filmeLocado.getFilme().getId();
      qtdDevolvida = filmeLocado.getQuantidadeLocada();
      filmeDevolvido = this.filmeService.buscarAcrescentarEstoque(filmeId, qtdDevolvida);
      System.out.println("ESTOQUE 2 ="+filmeDevolvido.getQuantidadeEstoque());
      filmesDevolvidos.add(filmeDevolvido);
    }
    this.filmeService.salvarTodos(filmesDevolvidos);
  }

  public Integer contarFilmesNasLocacoes(List<Locacao> locacoes) {
    Integer qtdFilmes = 0;
    for (Locacao locacao : locacoes) {
      qtdFilmes += contarFilmes(locacao.getFilmes());
    }
    return qtdFilmes;
  }

  public Integer contarFilmes(List<LocacaoTemFilme> filmesLocados) {
    Integer qtdFilmes = 0;
    for (LocacaoTemFilme filmeLocado : filmesLocados) {
      qtdFilmes += filmeLocado.getQuantidadeLocada();
    }
    return qtdFilmes;
  }

  public Double calcularValorTotalDiaria(LocacaoTemFilme locacaoTemFilme) {
    final Double valorDiaria = locacaoTemFilme.getFilme().getValorDiaria();
    final Integer qtdLocada = locacaoTemFilme.getQuantidadeLocada();
    final Double valorTotalDiaria = valorDiaria * qtdLocada;

    return valorTotalDiaria;
  }

}
