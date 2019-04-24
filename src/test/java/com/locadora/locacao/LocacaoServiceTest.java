package com.locadora.locacao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.locadora.infra.cliente.Cliente;
import com.locadora.infra.cliente.ClienteService;
import com.locadora.infra.enums.StatusLocacao;
import com.locadora.infra.filme.Filme;
import com.locadora.infra.genero.Genero;
import com.locadora.infra.locacao.Locacao;
import com.locadora.infra.locacao.LocacaoFilter;
import com.locadora.infra.locacao.LocacaoRepository;
import com.locadora.infra.locacao.LocacaoService;
import com.locadora.infra.locacao.exceptions.LocacaoLimiteDeFilmesException;
import com.locadora.infra.locacao.exceptions.LocacaoNaoEncontradaException;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilme;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilmeService;

@RunWith(MockitoJUnitRunner.class)
public class LocacaoServiceTest {

  @InjectMocks
  private LocacaoService locacaoService;

  @Mock
  private LocacaoRepository locacaoRepository;

  @Mock
  private ClienteService clienteService;

  @Mock
  private LocacaoTemFilmeService locacaoTemFilmeService;

  @Test
  public void testPesquisar() {
    LocacaoFilter filter = new LocacaoFilter();

    when(this.locacaoRepository.filtrar(filter)).thenReturn(this.criarListaLocacoes());

    List<Locacao> locacoes = this.locacaoService.pesquisar(filter);

    assertThat(locacoes, hasSize(3));
  }

  @Test
  public void testBuscarPorId() {
    Locacao locacaoSalva = new Locacao(1, null, null, null, null, null, null);

    when(this.locacaoRepository.findById(1)).thenReturn(Optional.of(locacaoSalva));

    Locacao locacaoBuscada = this.locacaoService.buscarPorId(1);

    assertThat(locacaoBuscada, equalTo(locacaoSalva));
  }

  @Test
  public void testBuscarPorId_LocacaoNaoEncontradaException() {

    when(this.locacaoRepository.findById(1)).thenReturn(Optional.ofNullable(null));

    try {
      this.locacaoService.buscarPorId(1);
    } catch (Exception e) {
      assertThat(e, instanceOf(LocacaoNaoEncontradaException.class));
    }


  }


  @Test
  public void testListarPorCPF() {

    Cliente cliente = new Cliente(2, "188.540.800-55", "Luis Lance", null);

    when(this.clienteService.buscarClientePorCpf(cliente.getCpf())).thenReturn(cliente);

    when(this.locacaoRepository.findByClienteAndStatus(cliente, StatusLocacao.ABERTO)).thenReturn(

        Arrays.asList(new Locacao(1, null, null, StatusLocacao.ABERTO, null, cliente, null),
            new Locacao(2, null, null, StatusLocacao.ABERTO, null, cliente, null)));

    List<Locacao> locacoesPendentes = this.locacaoService.listarPorCPF(cliente.getCpf());

    assertThat(locacoesPendentes, hasSize(2));

  }

  @Test
  public void testListarFilmes() {

    final Cliente cliente = new Cliente(2, "188.540.800-55", "Luis Lance", null);

    final List<LocacaoTemFilme> filmes =
        Arrays.asList(new LocacaoTemFilme(), new LocacaoTemFilme(), new LocacaoTemFilme());

    final Locacao locacao = new Locacao(1, null, null, StatusLocacao.ABERTO, null, cliente, filmes);

    when(this.locacaoRepository.findById(locacao.getId())).thenReturn(Optional.of(locacao));

    List<LocacaoTemFilme> filmesListados = this.locacaoService.listarFilmes(locacao.getId());

    assertThat(filmesListados, hasSize(3));

  }

  @Test
  public void testCriar() {

    final Locacao locacao = this.criarLocacaoValida();

    when(this.locacaoTemFilmeService.verificaFilmes(locacao.getFilmes()))
        .thenReturn(locacao.getFilmes());

    when(this.locacaoTemFilmeService.associarFilmes(locacao.getFilmes(), locacao))
        .thenReturn(locacao.getFilmes());

    when(this.clienteService.buscarPorId(locacao.getCliente().getId()))
        .thenReturn(locacao.getCliente());

    when(this.locacaoRepository.save(locacao)).thenReturn(locacao);

    Locacao locacaoSalva = this.locacaoService.criar(locacao);

    assertThat(locacaoSalva.getStatus(), equalTo(StatusLocacao.ABERTO));
    verify(this.locacaoRepository, times(2)).save(locacao);
  }

  @Test
  public void testAtualizar() {
    final Locacao locacaoSalva = this.criarLocacaoValida();

    final Locacao locacaoAAtualizar =
        new Locacao(1, null, null, StatusLocacao.ABERTO, null, new Cliente(), null);

    when(this.locacaoRepository.findById(1)).thenReturn(Optional.of(locacaoSalva));

    when(this.locacaoRepository.save(locacaoAAtualizar)).thenReturn(locacaoAAtualizar);

    Locacao locacaoAtualizada = this.locacaoService.atualizar(1, locacaoAAtualizar);

    assertThat(locacaoAtualizada, equalTo(locacaoAAtualizar));
  }

  @Test
  public void testDevolverLocacao() {
    final Locacao locacaoDevolvida = this.criarLocacaoValida();

    when(this.locacaoRepository.findById(locacaoDevolvida.getId()))
        .thenReturn(Optional.of(locacaoDevolvida));

    this.locacaoService.devolverLocacao(locacaoDevolvida.getId());


    verify(this.locacaoRepository, times(1)).save(locacaoDevolvida);
  }

  @Test
  public void buscarClienteValido_LocacaoLimiteDeFilmesException() {
    final Locacao LocacaoDoCliente = this.criarLocacaoValida();
    final Locacao LocacaoRealizada = this.criarLocacaoValida();
        
    final List<Locacao> locacoes = Arrays.asList(LocacaoDoCliente);
       
    when(this.clienteService.buscarPorId(1))
      .thenReturn(LocacaoDoCliente.getCliente());
    
    when(this.locacaoRepository.findByClienteAndStatus(LocacaoRealizada.getCliente(), StatusLocacao.ABERTO))
      .thenReturn(locacoes);
    
    when(this.locacaoTemFilmeService.contarFilmesNasLocacoes(locacoes))
      .thenReturn(3);
    
    when(this.locacaoTemFilmeService.contarFilmes(LocacaoRealizada.getFilmes()))
      .thenReturn(3);
    
    try {
      this.locacaoService.buscarClienteValido(LocacaoRealizada);
    } catch (Exception e) {
      assertThat(e, instanceOf(LocacaoLimiteDeFilmesException.class));
    }
    
  }
  
  @Test
  public void testCalcularValorTotal() {

    List<LocacaoTemFilme> filmes =
        Arrays.asList(new LocacaoTemFilme(new Locacao(), new Filme(), null, 10.0),
            new LocacaoTemFilme(new Locacao(), new Filme(), null, 5.0));

    @SuppressWarnings("deprecation")
    final Locacao locacao =
        new Locacao(1, new Date(2019, 04, 01), new Date(2019, 04, 05), null, null, null, filmes);

    Double valorTotal = this.locacaoService.calcularValorTotal(locacao);

    assertThat(valorTotal, equalTo(75.0));
  }

  @Test
  public void testExcluir() {

    Locacao locacaoAExcluir = this.criarLocacaoValida();

    when(this.locacaoRepository.findById(1)).thenReturn(Optional.of(locacaoAExcluir));

    this.locacaoService.excluir(locacaoAExcluir.getId());

    verify(this.locacaoTemFilmeService, times(1)).devolverAoEstoque(locacaoAExcluir.getFilmes());
    verify(this.locacaoRepository, times(1)).deleteById(locacaoAExcluir.getId());

  }

  private List<Locacao> criarListaLocacoes() {

    Cliente cliente = new Cliente(2, "188.540.800-55", "Luis Lance", null);

    return Arrays.asList(new Locacao(1, null, null, StatusLocacao.ABERTO, null, cliente, null),
        new Locacao(2, null, null, StatusLocacao.ABERTO, null, cliente, null),
        new Locacao(3, null, null, StatusLocacao.FINALIZADO, null, cliente, null));

  }

  private Locacao criarLocacaoValida() {
    Cliente cliente = new Cliente(1, "188.540.800-55", "Luis Lance", null);

    final Filme filme1 = new Filme(1, "Matrix", 90, 10, "Neo", "Washolski", 8.0, new Genero());
    final Filme filme2 = new Filme(2, "Superbad", 90, 10, "", "Mclovin", 8.0, new Genero());

    List<LocacaoTemFilme> filmesLocados =
        Arrays.asList(
            new LocacaoTemFilme(new Locacao(), filme1,2, 10.0),
            new LocacaoTemFilme(new Locacao(), filme2,1, 10.0));


    return new Locacao(1, new Date(System.currentTimeMillis()), null, null, null, cliente,
        filmesLocados);
  }

}
