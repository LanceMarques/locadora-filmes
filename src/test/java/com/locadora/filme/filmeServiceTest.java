package com.locadora.filme;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.locadora.infra.filme.Filme;
import com.locadora.infra.filme.FilmeRepository;
import com.locadora.infra.filme.FilmeService;
import com.locadora.infra.filme.exceptions.FilmeEstoqueIndisponivelException;
import com.locadora.infra.filme.exceptions.FilmeNaoEncontradoException;
import com.locadora.infra.genero.Genero;
import com.locadora.infra.genero.GeneroService;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilmeService;

@RunWith(MockitoJUnitRunner.class)
public class filmeServiceTest {

  @InjectMocks
  private FilmeService filmeService;

  @Mock
  private FilmeRepository filmeRepository;

  @Mock
  private GeneroService generoService;

  @Mock
  private LocacaoTemFilmeService locacaoTemfilmeService;

  @Before
  public void setUp() throws Exception {}

  @Test
  public void testListarTodos() {
    when(this.filmeRepository.findAll()).thenReturn(this.listarFilmesExistentes());
    List<Filme> filmes = filmeService.listarTodos();
    assertThat(filmes, hasSize(3));
  }

  @Test
  public void testListarPorGenero() {
    Filme filmeExistente = this.criarNovoFilme(1, "matrix", 90, 10, "Neo", "Washolski", 8.0, new Genero(1,"Terror"));
    
    when(this.filmeRepository.findByGenero(filmeExistente.getGenero()))
      .thenReturn(Arrays.asList(filmeExistente));
    
    List<Filme> filmes = this.filmeService.listarPorGenero(filmeExistente.getGenero());
    
    assertThat(filmes, hasSize(1));
    
  }

  @Test
  public void testBuscarPorId() {
    Filme filmeExistente = this.criarNovoFilme(1, "matrix", 90, 10, "Neo", "Washolski", 8.0, new Genero(1,"Terror"));

    when(this.filmeRepository.findById(1)).thenReturn(Optional.of(filmeExistente));

    Filme filmeBuscado = this.filmeService.buscarPorId(1);

    assertThat(filmeBuscado, equalTo(filmeExistente));
  }

  @Test
  public void testBuscarPorId_FilmeNaoEncontradoException() {
    Filme filmeBuscado = null;
    
    when(this.filmeRepository.findById(1)).thenReturn(Optional.ofNullable(null));

    try {
      filmeBuscado = this.filmeService.buscarPorId(1);  
    } catch (Exception e) {
      assertThat(e, IsInstanceOf.instanceOf(FilmeNaoEncontradoException.class));
    }
    assertThat(filmeBuscado, equalTo(null));
  }

  
  @Test
  public void testBuscarPorIdComEstoqueDisponivel() {
    final Integer qtdLocada = 5;
    final Filme filmeLocado = this.criarNovoFilme(1, "matrix", 90, 10, "Neo", "Washolski", 8.0, new Genero(1,"Terror"));
    
    when(this.filmeRepository.findById(1)).thenReturn(Optional.of(filmeLocado));
    Filme filmeBuscado = this.filmeService.buscarPorIdComEstoqueDisponivel(1, qtdLocada);
    assertThat(filmeBuscado.getQuantidadeEstoque(), greaterThanOrEqualTo(qtdLocada));
  }

  @Test
  public void testBuscarPorIdComEstoqueDisponivel_FilmeEstoqueIndisponivelException() {
    final Integer qtdLocada = 5;
    final Filme filmeLocado = this.criarNovoFilme(1, "matrix", 90, 4, "Neo", "Washolski", 8.0, new Genero(1,"Terror"));
    final Filme filmeBuscado;
    
    when(this.filmeRepository.findById(1)).thenReturn(Optional.of(filmeLocado));
    
      try {
        filmeBuscado = this.filmeService.buscarPorIdComEstoqueDisponivel(1, qtdLocada); 
      } catch (Exception e) {
        assertThat(e, IsInstanceOf.instanceOf(FilmeEstoqueIndisponivelException.class));
      }
    assertThat(filmeBuscado,em);
  }
  
  @Test
  public void testBuscarReduzirEstoque() {
    final Integer qtdLocada = 5;

    final Filme filmeModelo = this.criarNovoFilme(1, "matrix", 90, 10, "Neo", "Washolski", 8.0, new Genero(1,"Terror"));
    
    when(this.filmeRepository.findById(1)).thenReturn(Optional.of(filmeModelo));

    final Filme filmeComEstoque = this.filmeService.buscarPorIdComEstoqueDisponivel(1, qtdLocada);
    final Integer estoqueInicial = filmeComEstoque.getQuantidadeEstoque();
    final Filme filmeEstoqueReduzido = this.filmeService.buscarReduzirEstoque(1, qtdLocada);
    final Integer estoqueFinal = filmeEstoqueReduzido.getQuantidadeEstoque();

    assertThat(estoqueInicial - qtdLocada, equalTo(estoqueFinal));
  }

  @Test
  public void testBuscarAcrescentarEstoque() {

    final Integer qtdDevolvida = 5;
    final Filme filmeModelo = this.criarNovoFilme(1, "matrix", 90, 10, "Neo", "Washolski", 8.0, new Genero(1,"Terror"));
    
    when(this.filmeRepository.findById(1)).thenReturn(Optional.of(filmeModelo));

    final Filme filmeComEstoque =
        this.filmeService.buscarPorIdComEstoqueDisponivel(1, qtdDevolvida);
    final Integer estoqueInicial = filmeComEstoque.getQuantidadeEstoque();
    final Filme filmeEstoqueReduzido = this.filmeService.buscarAcrescentarEstoque(1, qtdDevolvida);
    final Integer estoqueFinal = filmeEstoqueReduzido.getQuantidadeEstoque();

    assertThat(estoqueInicial + qtdDevolvida, equalTo(estoqueFinal));

  }

  @Test
  public void testCriar() {
    final Filme filmeASalvar = this.criarNovoFilme(1, "matrix", 90, 10, "Neo", "Washolski", 8.0, new Genero(1,"Terror"));
    
    when(this.generoService.buscarPorId(filmeASalvar.getGenero().getId()))
        .thenReturn(filmeASalvar.getGenero());
    when(this.filmeRepository.save(filmeASalvar)).thenReturn(filmeASalvar);

    final Filme filmeSalvo = this.filmeService.criar(filmeASalvar);

    assertThat(filmeSalvo, equalTo(filmeASalvar));
  }

  @Test
  public void testSalvarTodos() {

  }

  @Test
  public void testAtualizar() {
    
    final Filme filmeSalvo = this.criarNovoFilme(1, "Matrix", 90, 10, "Neo", "Washolski", 8.0, new Genero(1,"Terror"));
    final Filme filmeAtualizar = this.criarNovoFilme(1, "Terminator", 120, 10, "Neo", "Washolski", 8.0, new Genero(1,"Ficcao"));
    
    when(this.filmeRepository.findById(filmeSalvo.getId()))
      .thenReturn(Optional.of(filmeSalvo));
    
    when(this.filmeRepository.save(filmeSalvo)).thenReturn(filmeSalvo);
    
    final Filme filmeAtualizado = this.filmeService.atualizar(filmeAtualizar.getId(), filmeAtualizar);
    
    assertThat(filmeAtualizado, equalTo(filmeAtualizar));
    
  }

  @Test
  public void testExcluir() {

    final Filme filmeAExcluir = this.criarNovoFilme(1, "matrix", 90, 10, "Neo", "Washolski", 8.0, new Genero(1,"Terror"));
    when(this.locacaoTemfilmeService.listarPorFilme(filmeAExcluir)).thenReturn(Arrays.asList());
    when(this.filmeRepository.findById(filmeAExcluir.getId()))
        .thenReturn(Optional.of(filmeAExcluir));

    this.filmeService.excluir(filmeAExcluir.getId());

    verify(this.filmeRepository, times(1)).deleteById(filmeAExcluir.getId());

  }

  private List<Filme> listarFilmesExistentes() {
    Genero acao = new Genero(1, "acao");
    Genero ficcao = new Genero(2, "ficcao");
    Genero aventura = new Genero(3, "aventura");
    return Arrays.asList(new Filme(1, "matrix", 90, 10, "Neo", "Washolski", 8.0, ficcao),
        new Filme(2, "fight club", 120, 15, "Tyler", "Pallanuik", 10.5, acao),
        new Filme(3, "Forest Gump", 130, 5, "Run forest", "Steven", 5.5, aventura));
  }

  private Filme criarNovoFilme(Integer id, String titulo, Integer duracao, Integer quantidadeEstoque,
      String sinopse, String nomeDiretor, Double valorDiaria, Genero genero) {
    return new Filme(id, titulo, duracao, quantidadeEstoque, sinopse, nomeDiretor, valorDiaria, genero);    
  }

}