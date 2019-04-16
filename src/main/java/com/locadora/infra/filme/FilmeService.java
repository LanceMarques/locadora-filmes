package com.locadora.infra.filme;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.locadora.infra.filme.exceptions.FilmeEstoqueIndisponivelException;
import com.locadora.infra.filme.exceptions.FilmeNaoEncontradoException;
import com.locadora.infra.filme.exceptions.LocacaoAssociadaException;
import com.locadora.infra.genero.Genero;
import com.locadora.infra.genero.GeneroService;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilme;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilmeService;

/**
 * Classe responsavel por implementar as funcionalidades e validacoes solicitadas pelo controller da
 * entidade Filme
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@Service
public class FilmeService {

  @Autowired
  private FilmeRepository filmeRepository;
  @Autowired
  private GeneroService generoService;
  @Autowired
  private LocacaoTemFilmeService locacaoTemFilmeService;

  /**
   * Metodo responsavel por fornecer uma lista com todos os {@link Filme filmes} cadastrados no
   * sistema
   * 
   * @return Lista com todos os {@link Filme filmes} cadastrados
   */
  public List<Filme> listarTodos() {
    return this.filmeRepository.findAll();
  }

  /**
   * Metodo responsavel por fornecer uma lista com todos os {@link Filme filmes} cadastrados no
   * sistema, que possuam o genero recebido como parametro
   * 
   * @return {@link List Lista} com todos os {@link Filme filmes} cadastrados
   */
  public List<Filme> listarPorGenero(Genero genero) {
    return this.filmeRepository.findByGenero(genero);
  }

  /**
   * Metodo responsavel por fornecer um {@link Filme filme} que possui o id informado.
   * 
   * @param id ({@link Integer}) Id requisitado na pesquisa.
   * @return {@link Optional Optional} contendo um {@link Filme filme} com o id pesquisado.
   * 
   * @since 1.0.0
   */
  public Filme buscarPorId(Integer id) {
    final Optional<Filme> filmeSalvo = this.filmeRepository.findById(id);
    if (!filmeSalvo.isPresent()) {
      throw new FilmeNaoEncontradoException();
    }
    return filmeSalvo.get();
  }

  /**
   * Metodo responsavel por fornecer um {@link Filme filme} que possui o id informado e estoque
   * superior a quantidade locada recebida como parametro.
   * 
   * @param id ({@link Integer}) Id requisitado na pesquisa
   * @param quantidadeLocada ({@link Integer}}) Quantidade de filmes na locacao
   * @return Filme{@link Filme}} que possui o id informado e quantidade de estoque disponivel para
   *         locacao.
   * 
   * @since 1.0.0
   */
  public Filme buscarPorIdComEstoqueDisponivel(Integer id, int quantidadeLocada) {
    final Filme filmeSalvo = this.buscarPorId(id);
    if (filmeSalvo.getQuantidadeEstoque() < quantidadeLocada) {
      throw new FilmeEstoqueIndisponivelException();
    }
    return filmeSalvo;
  }

  /**
   * Metodo responsavel por buscar e reduzir o estoque de um {@link Filme filme} com base na
   * quantidade locada recebida como parametro.
   * 
   * @param filmeId ({@link Integer}) Id requisitado na pesquisa.
   * @param quantidadeLocada ({@link Integer}}) Quantidade de filmes na locacao.
   * @return Filme{@link Filme}} com a quantidade de estoque reduzida.
   * 
   * @since 1.0.0
   */
  public Filme buscarReduzirEstoque(Integer filmeId, Integer quantidadeLocada) {
    Filme filmeSalvo = this.buscarPorIdComEstoqueDisponivel(filmeId, quantidadeLocada);
    final Integer estoqueDisponivel = filmeSalvo.getQuantidadeEstoque() - quantidadeLocada;
    filmeSalvo.setQuantidadeEstoque(estoqueDisponivel);
    return filmeSalvo;
  }

  /**
   * Metodo responsavel por buscar um {@link Filme filme} e acrescentar a quantidade devolvida ao
   * estoque com base no parametro recebido.
   * 
   * @param filmeId {@link Integer} Id requisitado na pesquisa.
   * @param quantidadeDevolvida ({@link Integer}}) Quantidade de filmes devolvidos.
   * @return Filme{@link Filme}} com a quantidade de estoque reduzida.
   * 
   * @since 1.0.0
   */
  public Filme buscarAcrescentarEstoque(Integer filmeId, Integer quantidadeDevolvida) {
    Filme filmeSalvo = this.buscarPorId(filmeId);
    final Integer estoqueDisponivel = filmeSalvo.getQuantidadeEstoque() + quantidadeDevolvida;
    filmeSalvo.setQuantidadeEstoque(estoqueDisponivel);
    return filmeSalvo;
  }

  /**
   * Metodo responsavel por cadastrar um filme recebido como parametro
   * 
   * @param filme ({@link Filme}) filme recebido como parametro para cadastro no sistema
   * @return filmeSalvo{@link Filme} Filme{@link Filme} cadastrado no sistema.
   * 
   * @since 1.0.0
   */
  public Filme criar(Filme filme) {
    final Integer generoId = filme.getGenero().getId();
    final Genero generoSalvo = generoService.buscarPorId(generoId);
    Filme filmeSalvo = filme;

    filmeSalvo.setGenero(generoSalvo);
    return this.filmeRepository.save(filmeSalvo);
  }

  /**
   * Metodo responsavel por salvar uma lista de {@link Filme filmes} recebida como parametro
   * 
   * @param filmes {@link List} Lista de {@link Filme filmes} que serao salvos
   */
  public void salvarTodos(List<Filme> filmes) {
    this.filmeRepository.saveAll(filmes);
  }

  /**
   * Metodo responsavel por atualizar um {@link Filme filme} que possui o id solicitado com os dados
   * recebidos no corpo da requisicao.
   * 
   * @param id {@link Integer} Id do filme a ser atualizado.
   * @param filme {@link Filme} Dados atualizados.
   * 
   * @since 1.0.0
   */
  public Filme atualizar(Integer id, Filme filme) {
    Filme filmeSalvo = buscarPorId(id);
    BeanUtils.copyProperties(filme, filmeSalvo, "id");
    return this.filmeRepository.save(filmeSalvo);
  }

  /**
   * Metodo responsavel por excluir um {@link Filme filme} que possui o id informado.
   * 
   * @param id {@link Integer} Id do filme a ser excluido.
   * 
   * @since 1.0.0
   */
  public void excluir(Integer id) {
    final Filme filmeSalvo = buscarPorId(id);
    verificarLocacaoAssociada(filmeSalvo);
    this.filmeRepository.deleteById(filmeSalvo.getId());
  }

  /**
   * Metodo responsavel por verificar se há alguma locacao associada ao filme recebido como
   * parametro
   * 
   * @param filme {@link Filme} Filme recebido como parametro para consulta
   * 
   * @since 1.0.0
   */
  private void verificarLocacaoAssociada(final Filme filmeSalvo) {
    final List<LocacaoTemFilme> locacoesAssociadas;
    locacoesAssociadas = this.locacaoTemFilmeService.listarPorFilme(filmeSalvo);
    if (!locacoesAssociadas.isEmpty()) {
      throw new LocacaoAssociadaException();
    }
  }
}
