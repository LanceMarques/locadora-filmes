package com.locadora.genero;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.locadora.infra.filme.Filme;
import com.locadora.infra.filme.FilmeService;
import com.locadora.infra.genero.Genero;
import com.locadora.infra.genero.GeneroRepository;
import com.locadora.infra.genero.GeneroService;
import com.locadora.infra.genero.exceptions.FilmeAssociadoException;
import com.locadora.infra.genero.exceptions.GeneroJaCadastradoException;
import com.locadora.infra.genero.exceptions.GeneroNaoEncontradoException;

@RunWith(MockitoJUnitRunner.class)
public class GeneroServiceTest {

  @InjectMocks
  private GeneroService generoService;

  @Mock
  private GeneroRepository generoRepository;

  @Mock
  private FilmeService filmeService;

  @Test
  public void testListarTodos() {

    when(this.generoRepository.findAll()).thenReturn(this.listarGenerosExistentes());

    List<Genero> generos = this.generoService.listarTodos();

    assertThat(generos, hasSize(3));
  }

  @Test
  public void testBuscarPorId() {
    Genero generoExistente = this.buscarGeneroExistente().get();
    when(this.generoRepository.findById(1)).thenReturn(this.buscarGeneroExistente());

    Genero generoBuscado = this.generoService.buscarPorId(1);
    assertThat(generoBuscado, equalTo(generoExistente));
  }

  @Test
  public void testBuscarPorId_GeneroNaoEncontradoException() {
    when(this.generoRepository.findById(1)).thenReturn(Optional.ofNullable(null));

    try {
      this.generoService.buscarPorId(1);
    } catch (Exception e) {
      assertThat(e, IsInstanceOf.instanceOf(GeneroNaoEncontradoException.class));
    }
  }

  @Test
  public void testCriarGenero() {
    final Genero generoASalvar = this.criarNovoGenero(1, "Terror");

    when(this.generoRepository.findByNome(generoASalvar.getNome()))
        .thenReturn(this.buscarGeneroInexistente());
    when(this.generoRepository.save(generoASalvar)).thenReturn(generoASalvar);

    final Genero generoSalvo = this.generoService.criar(generoASalvar);

    assertThat(generoSalvo, equalTo(generoASalvar));
  }

  @Test
  public void testCriarGenero_GeneroJaCadastradoException() {
    final Genero generoASalvar = this.criarNovoGenero(1, "Terror");

    when(this.generoRepository.findByNome(generoASalvar.getNome()))
        .thenReturn(Optional.of(generoASalvar)); 
    try {
      this.generoService.criar(generoASalvar);
    } catch (Exception e) {
      assertThat(e, IsInstanceOf.instanceOf(GeneroJaCadastradoException.class));
    }
  }
  
  @Test
  public void testAtualizar() {
    final Genero generoAtualizado;
    final Genero generoAAtualizar = this.criarNovoGenero(1, "Comedia");
    final Genero generoSalvo = this.criarNovoGenero(1, "Terror");

    when(this.generoRepository.findById(generoAAtualizar.getId()))
        .thenReturn(Optional.of(generoSalvo));

    when(this.generoRepository.findByNome(generoAAtualizar.getNome()))
        .thenReturn(this.buscarGeneroInexistente());

    when(this.generoRepository.save(generoSalvo)).thenReturn(generoSalvo);

    generoAtualizado = this.generoService.atualizar(generoAAtualizar.getId(), generoAAtualizar);

    assertThat(generoAtualizado, equalTo(generoAAtualizar));
  }

  @Test
  public void testAtualizar_GeneroJaCadastradoException() {
    final Genero generoAAtualizar = this.criarNovoGenero(1, "Comedia");
    final Genero generoSalvo = this.criarNovoGenero(1, "Terror");
    final Genero generoJaCadastrado= this.criarNovoGenero(2, "Comedia");
    
    when(this.generoRepository.findById(generoAAtualizar.getId()))
        .thenReturn(Optional.of(generoSalvo));

    when(this.generoRepository.findByNome(generoAAtualizar.getNome()))
        .thenReturn(Optional.of(generoJaCadastrado));

    try {
        this.generoService.atualizar(generoAAtualizar.getId(), generoAAtualizar);  
    } catch (Exception e) {
      assertThat(e, IsInstanceOf.instanceOf(GeneroJaCadastradoException.class));
    }
  }
  
  @Test
  public void testExcluir() {
    final Genero generoAExcluir = this.criarNovoGenero(1, "Terror");
    when(this.filmeService.listarPorGenero(generoAExcluir)).thenReturn(Arrays.asList());
    when(this.generoRepository.findById(1)).thenReturn(Optional.of(generoAExcluir));
    this.generoService.excluir(1);
    verify(this.generoRepository, times(1)).deleteById(generoAExcluir.getId());
  }

  @Test
  public void testExcluir_FilmeAssociadoException() {
    final Genero generoAExcluir = this.criarNovoGenero(1, "Terror");
    when(this.filmeService.listarPorGenero(generoAExcluir)).thenReturn(Arrays.asList(new Filme()));
    when(this.generoRepository.findById(1)).thenReturn(Optional.of(generoAExcluir));
    try {
      this.generoService.excluir(1);      
    } catch (Exception e) {
      assertThat(e, IsInstanceOf.instanceOf(FilmeAssociadoException.class));
    }
    verify(this.generoRepository, times(0)).deleteById(generoAExcluir.getId());
  }
  
  private Genero criarNovoGenero(Integer id, String nome) {
    return new Genero(id, nome);
  }

  private Optional<Genero> buscarGeneroExistente() {
    final Genero generoExistente = new Genero(1, "Terror");
    return Optional.of(generoExistente);
  }

  private Optional<Genero> buscarGeneroInexistente() {
    return Optional.ofNullable(null);
  }

  private List<Genero> listarGenerosExistentes() {
    return Arrays.asList(new Genero(1, "Terror"), new Genero(2, "Comedia"),
        new Genero(3, "Aventura"));
  }

}
