package com.locadora.infra.genero;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.locadora.infra.filme.Filme;
import com.locadora.infra.filme.FilmeService;
import com.locadora.infra.genero.exceptions.FilmeAssociadoException;
import com.locadora.infra.genero.exceptions.GeneroJaCadastradoException;
import com.locadora.infra.genero.exceptions.GeneroNaoEncontradoException;

/**
 * Classe responsavel por implementar as funcionalidades e validacoes solicitadas pelo controller da
 * entidade Genero
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@Service
public class GeneroService {

  @Autowired
  private GeneroRepository generoRepository;

  @Autowired
  private FilmeService filmeService;

  /**
   * Metodo responsavel por fornecer uma lista com todos os {@link Genero generos} cadastrados no
   * sistema
   * 
   * @return Lista com todos os {@link Genero generos} cadastrados
   */
  public List<Genero> listarTodos() {
    return this.generoRepository.findAll();
  }

  /**
   * Metodo responsavel por fornecer um {@link Genero genero} que possui o id informado.
   * 
   * @param id ({@link Integer}) Id requisitado na pesquisa
   * @return Genero{@link Genero}} que possui o id informado.
   * 
   * @since 1.0.0
   */
  public Genero buscarPorId(Integer id) {
    final Optional<Genero> generoOpt = this.generoRepository.findById(id);
    if (!generoOpt.isPresent()) {
      throw new GeneroNaoEncontradoException();
    }
    return generoOpt.get();
  }

  /**
   * Metodo responsavel por cadastrar um genero recebido como parametro
   * 
   * @param genero({@link Genero}) filme recebido como parametro para cadastro no sistema
   * @return generoSalvo{@link Genero} genero{@link Genero} cadastrado no sistema.
   * 
   * @since 1.0.0
   */
  public Genero criar(Genero genero) {
    final Optional<Genero> generoSalvo = buscarPorNome(genero.getNome());
    if (generoSalvo.isPresent()) {
      throw new GeneroJaCadastradoException();
    }
    return this.generoRepository.save(genero);
  }

  /**
   * Metodo responsavel por atualizar um {@link Genero genero} que possui o id solicitado com os dados
   * recebidos no corpo da requisicao.
   * 
   * @param id ({@link Integer}) Id do genero a ser atualizado.
   * @param genero{@link Genero} Dados atualizados.
   * 
   * @since 1.0.0
   */
  public Genero atualizar(Integer id, Genero genero) {
    final Genero generoSalvo = buscarPorId(id);
    final Optional<Genero> generoRepetido = buscarPorNome(genero.getNome());
    if (generoRepetido.isPresent() && generoRepetido.get().getId() != id) {
      throw new GeneroJaCadastradoException();
    }
    BeanUtils.copyProperties(genero, generoSalvo, "id");
    return this.generoRepository.save(generoSalvo);
  }

  /**
   * Metodo responsavel por excluir um genero{@link Genero} que possui o id informado.
   * 
   * @param id ({@link Integer}) Id do genero a ser excluido.
   * 
   * @since 1.0.0
   */
  public void excluir(Integer id) {
    final Genero genero = buscarPorId(id);
    final List<Filme> filmesAssociados = filmeService.listarPorGenero(genero);
    if (filmesAssociados.isEmpty()) {
      this.generoRepository.deleteById(genero.getId());
    } else {
      throw new FilmeAssociadoException();
    }
  }

  /**
   * Metodo responsavel por fornecer um genero{@link Genero} que possui o nome recebido como parametro.
   * 
   * @param nome({@link String}) Nome requisitado na pesquisa
   * @return generoSalvo{@link Genero}} que possui o id informado.
   * 
   * @since 1.0.0
   */
  private Optional<Genero> buscarPorNome(String nome) {
    final Optional<Genero> generoSalvo = this.generoRepository.findByNome(nome);
    return generoSalvo;
  }
}
