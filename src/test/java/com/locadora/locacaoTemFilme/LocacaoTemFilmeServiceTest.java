package com.locadora.locacaoTemFilme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.locadora.infra.filme.Filme;
import com.locadora.infra.filme.FilmeService;
import com.locadora.infra.genero.Genero;
import com.locadora.infra.locacao.Locacao;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilme;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilmeRepository;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilmeService;

@RunWith(MockitoJUnitRunner.class)
public class LocacaoTemFilmeServiceTest {

  @InjectMocks
  private LocacaoTemFilmeService locacaoTemFilmeService;

  @Mock
  private LocacaoTemFilmeRepository locacaoTemFilmeRepository;

  @Mock
  private FilmeService filmeService;

  @Test
  public void testListarPorFilme() {
    Filme filme = new Filme();

    when(this.locacaoTemFilmeRepository.findByFilme(filme))
        .thenReturn(this.criarListaLocacaoTemFilme());

    List<LocacaoTemFilme> filmesLocados = this.locacaoTemFilmeService.listarPorFilme(filme);

    assertThat(filmesLocados, hasSize(3));
  }

  @Test
  public void testAssociarFilmes() {

    final Locacao locacao = new Locacao(1, null, null, null, null, null, null);

    final Integer locacaoId = locacao.getId();
    Integer locacaoIdAssociado;

    List<LocacaoTemFilme> filmesLocados = this.criarListaLocacaoTemFilme();

    List<LocacaoTemFilme> filmesAssociados =
        this.locacaoTemFilmeService.associarFilmes(filmesLocados, locacao);

    for (LocacaoTemFilme locacaoTemFilme : filmesAssociados) {
      locacaoIdAssociado = locacaoTemFilme.getId().getLocacaoId();
      assertThat(locacaoIdAssociado, equalTo(locacaoId));
    }

  }

  @Test
  public void testContarFilmesNasLocacoes() {
    List<Locacao> locacoes = Arrays
        .asList(
              new Locacao(1, null, null, null, null, null, this.criarListaLocacaoTemFilme())
            );
    Integer totalFilmes = this.locacaoTemFilmeService.contarFilmesNasLocacoes(locacoes);
    
    assertThat(totalFilmes,equalTo(5));
    
  }

  @Test
  public void testContarFilmes() {

    List<LocacaoTemFilme> filmesLocados = this.criarListaLocacaoTemFilme();
    
    Integer totalFilmes = this.locacaoTemFilmeService.contarFilmes(filmesLocados);
    
    assertThat(totalFilmes,equalTo(5));
    
  }

  private List<LocacaoTemFilme> criarListaLocacaoTemFilme() {

    Genero acao = new Genero(1, "Acao");
    Filme filme1 = new Filme(1, "fight club", 120, 15, "Tyler", "Pallanuik", 10.0, acao);

    return Arrays.asList(new LocacaoTemFilme(new Locacao(), filme1, 2),
        new LocacaoTemFilme(new Locacao(), filme1, 2),
        new LocacaoTemFilme(new Locacao(), filme1, 1));

  }

}
