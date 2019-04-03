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

@Service
public class FilmeService {

  @Autowired
  private FilmeRepository filmeRepository;
  @Autowired
  private GeneroService generoService;
  @Autowired
  private LocacaoTemFilmeService locacaoTemFilmeService;
  
  public List<Filme> listarTodos() {
    return this.filmeRepository.findAll();
  }

  public Filme buscarPorId(Integer id) {
    final Optional<Filme> filmeSalvo = this.filmeRepository.findById(id);
    if (!filmeSalvo.isPresent()) {
      throw new FilmeNaoEncontradoException();
    }
    return filmeSalvo.get();
  }

  public Filme buscarPorIdComEstoqueDisponivel(Integer id, int quantidadeLocada) {
    final Filme filmeSalvo = this.buscarPorId(id);
    if (filmeSalvo.getQuantidadeEstoque() < quantidadeLocada) {
      throw new FilmeEstoqueIndisponivelException();
    }
    return filmeSalvo;
  }

  public Filme buscarReduzirEstoque(Integer filmeId, Integer quantidadeLocada) {
    Filme filmeSalvo = this.buscarPorIdComEstoqueDisponivel(filmeId, quantidadeLocada);
    final Integer estoqueDisponivel = filmeSalvo.getQuantidadeEstoque() - quantidadeLocada;
    filmeSalvo.setQuantidadeEstoque(estoqueDisponivel);
    return filmeSalvo;
  }

  public Filme buscarAcrescentarEstoque(Integer filmeId, Integer quantidadeDevolvida) {
    Filme filmeSalvo = this.buscarPorId(filmeId);
    final Integer estoqueDisponivel = filmeSalvo.getQuantidadeEstoque() + quantidadeDevolvida;
    filmeSalvo.setQuantidadeEstoque(estoqueDisponivel);
    return filmeSalvo;
  }

  public List<Filme> listarPorGenero(Genero genero) {
    return this.filmeRepository.findByGenero(genero);
  }

  public Filme criar(Filme filme) {
    final Integer generoId = filme.getGenero().getId();
    final Genero generoSalvo = generoService.buscarPorId(generoId);
    Filme filmeSalvo = filme;

    filmeSalvo.setGenero(generoSalvo);
    return this.filmeRepository.save(filmeSalvo);
  }

  public void salvarTodos(List<Filme> filmes) {
    this.filmeRepository.saveAll(filmes);
  }

  public Filme atualizar(Integer id, Filme filme) {
    Filme filmesalvo = buscarPorId(id);
    BeanUtils.copyProperties(filme, filmesalvo, "id");
    return this.filmeRepository.save(filmesalvo);
  }

  public void excluir(Integer id) {
    final Filme filmeSalvo = buscarPorId(id);
    
    final List<LocacaoTemFilme> locacoesAssociadas;
    
    locacoesAssociadas = this.locacaoTemFilmeService.listarPorFilme(filmeSalvo);
    if(!locacoesAssociadas.isEmpty()) {
      throw new LocacaoAssociadaException();
    }
    this.filmeRepository.deleteById(filmeSalvo.getId());
  }
  
  

}