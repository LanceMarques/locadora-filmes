package com.locadora.genero;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Equals;
import org.mockito.junit.MockitoJUnitRunner;
import com.locadora.infra.genero.Genero;
import com.locadora.infra.genero.GeneroRepository;
import com.locadora.infra.genero.GeneroService;

@RunWith(MockitoJUnitRunner.class)
public class GeneroServiceTest {

  @InjectMocks
  private GeneroService generoService;

  @Mock
  private GeneroRepository generoRepository;

  @Before
  public void setUp() throws Exception {


  }

  @Test
  public void testListarTodos() {

    when(this.generoRepository.findAll()).thenReturn(this.listarGenerosExistentes());

    List<Genero> generos = this.generoService.listarTodos();

    assertThat(generos, hasSize(3));
  }

  @Test
  public void testBuscarPorIdExistente() {
    Genero generoExistente = this.buscarGeneroExistente().get();
    when(this.generoRepository.findById(1)).thenReturn(this.buscarGeneroExistente());

    Genero generoBuscado = this.generoService.buscarPorId(1);
    assertThat(generoBuscado, equalTo(generoExistente));
  }

  @Test
  public void testCriarGeneroInexistente() {
    final Genero generoASalvar = this.criarNovoGenero(1, "Terror");

    when(this.generoRepository.findByNome(generoASalvar.getNome()))
        .thenReturn(this.buscarGeneroInexistente());
    when(this.generoRepository.save(generoASalvar)).thenReturn(generoASalvar);

    final Genero generoSalvo = this.generoService.criar(generoASalvar);

    assertThat(generoSalvo, equalTo(generoASalvar));
  }

  @Test
  public void testAtualizarNomeInexistente() {
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
  public void testExcluir() {

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
