package com.locadora.infra.locacaoTemFilme;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.locadora.infra.filme.Filme;
import com.locadora.infra.filme.FilmeService;
import com.locadora.infra.locacao.Locacao;

/**
 * Classe responsavel por implementar as funcionalidades e validacoes referentes ao relacionamento
 * entre as entidades Locacao e Filme
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@Service
public class LocacaoTemFilmeService {

  @Autowired
  private LocacaoTemFilmeRepository locacaoTemFilmeRepository;
  @Autowired
  private FilmeService filmeService;

  public List<LocacaoTemFilme> listarPorFilme(Filme filme) {
    return this.locacaoTemFilmeRepository.findByFilme(filme);
  }

  /**
   * Metodo responsavel por calcular o valor total da diaria de cada filme presente em uma Locacao.
   * 
   * @param filmes {@link List} Lista de {@link LocacaoTemFilme filmes} locados.
   */
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

  /**
   * Metodo responsavel por gerar o id composto de cada {@link LocacaoTemFilme filme} locado,
   * instanciando cada filme com a locacao recebida como parametro.
   * 
   * @param filmesLocados {@link List} Lista de {@link LocacaoTemFilme filmes} locados.
   * @param locacao {@link Locacao} Locacao a ser associada aos filmes locados.
   * 
   * @return {@link List Lista} com todos os {@link LocacaoTemFilme filmes} locados com os IDs
   *         associados a respectiva locacao.
   */
  public List<LocacaoTemFilme> associarFilmes(List<LocacaoTemFilme> filmesLocados,
      Locacao locacao) {

    int qtdEstoque;
    Filme filmeDaLocacao;

    List<LocacaoTemFilme> filmes = new ArrayList<>();
    List<Filme> filmesDasLocacoes = new ArrayList<>();

    for (LocacaoTemFilme filmeLocado : filmesLocados) {

      qtdEstoque = filmeLocado.getQuantidadeLocada();
      filmeDaLocacao = filmeLocado.getFilme();

      filmeLocado = new LocacaoTemFilme(locacao, filmeDaLocacao, qtdEstoque);
      filmeLocado.setValorTotalDaDiaria(this.calcularValorTotalDiaria(filmeLocado));

      filmesDasLocacoes.add(filmeLocado.getFilme());
      filmes.add(filmeLocado);
    }
    this.filmeService.salvarTodos(filmesDasLocacoes);
    return filmes;
  }

  /**
   * Metodo responsavel por consultar a disponibilidade dos {@link LocacaoTemFilme filmes} locados,
   * retornando uma lista de filmes com a quantidade locada reduzida de seu estoque.
   * 
   * @param filmesLocados {@link List} Lista de {@link LocacaoTemFilme filmes} locados.
   * 
   * @return {@link List Lista} com os {@link LocacaoTemFilme filmes} locados com a quantidade de
   *         estoque atualizada.
   */
  public List<LocacaoTemFilme> verificaFilmes(List<LocacaoTemFilme> filmesLocados) {

    Integer filmeId;
    Integer qtdLocada;
    Filme filmeSalvo;

    List<LocacaoTemFilme> filmes = new ArrayList<>();

    for (LocacaoTemFilme filmeLocado : filmesLocados) {
      filmeId = filmeLocado.getFilme().getId();
      qtdLocada = filmeLocado.getQuantidadeLocada();

      filmeSalvo = this.filmeService.buscarReduzirEstoque(filmeId, qtdLocada);

      filmeLocado.setFilme(filmeSalvo);
      filmeLocado.setValorTotalDaDiaria(calcularValorTotalDiaria(filmeLocado));

      filmes.add(filmeLocado);
    }
    return filmes;
  }

  /**
   * Metodo responsavel por atualizar os {@link LocacaoTemFilme filmes} devolvidos, retornando a
   * quantidade locada ao estoque disponivel.
   * 
   * @param filmesLocados {@link List} Lista de {@link LocacaoTemFilme filmes} devolvidos.
   * 
   */
  public void devolverAoEstoque(List<LocacaoTemFilme> filmesLocados) {
    Filme filmeDevolvido;
    Integer filmeId;
    Integer qtdDevolvida;
    List<Filme> filmesDevolvidos = new ArrayList<>();

    for (LocacaoTemFilme filmeLocado : filmesLocados) {
      filmeId = filmeLocado.getFilme().getId();
      qtdDevolvida = filmeLocado.getQuantidadeLocada();
      filmeDevolvido = this.filmeService.buscarAcrescentarEstoque(filmeId, qtdDevolvida);
      filmesDevolvidos.add(filmeDevolvido);
    }
    this.filmeService.salvarTodos(filmesDevolvidos);
  }

  /**
   * Metodo responsavel por contar todos os filmes em uma lista de locacoes.
   * 
   * @param locacoes {@link List} Lista de {@link Locacao locacoes}.
   * 
   * @return qtdFilmes {@link Integer} Quantidade de filmes nas locacoes.
   */
  public Integer contarFilmesNasLocacoes(List<Locacao> locacoes) {
    Integer qtdFilmes = 0;
    for (Locacao locacao : locacoes) {
      qtdFilmes += contarFilmes(locacao.getFilmes());
    }
    return qtdFilmes;
  }

  /**
   * Metodo responsavel por contar todos os filmes em uma locacao.
   * 
   * @param filmesLocados {@link List} Lista de {@link LocacaoTemFilme filmes} locados.
   * 
   * @return qtdFilmes {@link Integer} Quantidade de filmes na locacao.
   */
  public Integer contarFilmes(List<LocacaoTemFilme> filmesLocados) {
    Integer qtdFilmes = 0;
    for (LocacaoTemFilme filmeLocado : filmesLocados) {
      qtdFilmes += filmeLocado.getQuantidadeLocada();
    }
    return qtdFilmes;
  }

  /**
   * Metodo responsavel por calcular o valor total da diaria de um {@link LocacaoTemFilme filme}
   * locado com base no valor da diaria e a quantidade locada.
   * 
   * @param locacaoTemFilme {@link LocacaoTemFilme} Filme locado.
   * 
   * @return valorTotalDiaria {@link Double} Valor total da diaria de um filme locado.
   */
  private Double calcularValorTotalDiaria(LocacaoTemFilme locacaoTemFilme) {
    final Double valorDiaria = locacaoTemFilme.getFilme().getValorDiaria();
    final Integer qtdLocada = locacaoTemFilme.getQuantidadeLocada();
    final Double valorTotalDiaria = valorDiaria * qtdLocada;

    return valorTotalDiaria;
  }

}
