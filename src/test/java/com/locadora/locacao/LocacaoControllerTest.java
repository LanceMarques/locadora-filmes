package com.locadora.locacao;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.locadora.infra.cliente.Cliente;
import com.locadora.infra.enums.StatusLocacao;
import com.locadora.infra.filme.Filme;
import com.locadora.infra.genero.Genero;
import com.locadora.infra.locacao.Locacao;
import com.locadora.infra.locacao.LocacaoController;
import com.locadora.infra.locacao.LocacaoFilter;
import com.locadora.infra.locacao.LocacaoService;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilme;
import com.locadora.utils.JsonUtils;

@RunWith(SpringRunner.class)
@WebMvcTest(LocacaoController.class)
public class LocacaoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LocacaoService locacaoService;

  @Test
  public void testListarTodas() throws Exception {

    final List<Locacao> locacoes = this.criarListaLocacoes();
    
    when(this.locacaoService.pesquisar(any(LocacaoFilter.class)))
      .thenReturn(locacoes);
    
    final RequestBuilder requestBuilder =
        MockMvcRequestBuilders
        .get("/locacoes")
          .accept(MediaType.APPLICATION_JSON)
          .param("statusLocacao","ABERTO");
    
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isOk())
      .andExpect(content().json(JsonUtils.objetoParaJson(locacoes)));
   
    verify(this.locacaoService,times(1)).pesquisar(any(LocacaoFilter.class));
  }

  @Test
  public void testBuscarPorId() throws Exception {

    final Locacao locacao = this.criarLocacao();

    when(this.locacaoService.buscarPorId(1)).thenReturn(locacao);

    final RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get("/locacoes/1").accept(MediaType.APPLICATION_JSON);

    this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
        .andExpect(content().json(JsonUtils.objetoParaJson(locacao)));

    verify(this.locacaoService,times(1)).buscarPorId(1);
  }

  @Test
  public void testListarFilmesPorId() throws Exception {

    final List<LocacaoTemFilme> filmes = this.criarLocacao().getFilmes();
    
    when(this.locacaoService.listarFilmes(1))
      .thenReturn(filmes);
    
    final RequestBuilder requestBuilder =
        MockMvcRequestBuilders
          .get("/locacoes/1/filmes")
          .accept(MediaType.APPLICATION_JSON);
    
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isOk())
      .andExpect(content().json(JsonUtils.objetoParaJson(filmes)));
    
    verify(this.locacaoService, times(1)).listarFilmes(1);
  }

  @Test
  public void testListarPorCliente() throws Exception {
    
    final List<Locacao> locacoes = this.criarListaLocacoes();
    
    final String cpf = "47657065087";
    
    when(this.locacaoService.listarPorCPF(cpf))
      .thenReturn(locacoes);

    final RequestBuilder requestBuilder =
        MockMvcRequestBuilders
        .get("/locacoes/cliente/cpf/"+cpf)
          .accept(MediaType.APPLICATION_JSON);
    
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isOk())
      .andExpect(content().json(JsonUtils.objetoParaJson(locacoes)));
    
    verify(this.locacaoService,times(1)).listarPorCPF(cpf);
  }

  @Test
  public void testCriar() throws Exception {

    final Locacao locacao = this.criarLocacao();
    
    when(this.locacaoService.criar(locacao))
      .thenReturn(locacao);
    
    final RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/locacoes")
        .accept(MediaType.APPLICATION_JSON)
        .content(JsonUtils.objetoParaJson(locacao))
        .contentType(MediaType.APPLICATION_JSON);
    
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isCreated())
      .andExpect(content().json(JsonUtils.objetoParaJson(locacao)));
    
    verify(this.locacaoService, times(1)).criar(locacao);
    
  }

  @Test
  public void testAtualizar() throws Exception {

    final Locacao locacao = this.criarLocacao();
    
    final RequestBuilder requestBuilder = 
        MockMvcRequestBuilders
          .put("/locacoes/1")
            .content(JsonUtils.objetoParaJson(locacao))
            .contentType(MediaType.APPLICATION_JSON);
    
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isNoContent());
    
    verify(this.locacaoService,times(1)).atualizar(1, locacao);
  }

  @Test
  public void testDevolverLocacao() throws Exception {

    final RequestBuilder requestBuilder = 
        MockMvcRequestBuilders
          .put("/locacoes/1/devolucao");

    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isNoContent());

    verify(this.locacaoService,times(1)).devolverLocacao(1);
  }

  @Test
  public void testExcluir() throws Exception {

    final RequestBuilder requestBuilder =
        MockMvcRequestBuilders
          .delete("/locacoes/1");
    
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isNoContent());
    
    verify(this.locacaoService, times(1)).excluir(1);
  }

  private Locacao criarLocacao() {

    final Genero genero = new Genero(1, "terror");
    final Filme filme = new Filme(1, "matrix", 120, 10, "sinopse", "nome Diretor", 5.0, genero);

    List<LocacaoTemFilme> filmes = Arrays.asList(new LocacaoTemFilme(new Locacao(), filme));

    return new Locacao(1, null, null, StatusLocacao.ABERTO, null, new Cliente(), filmes);
    
  }

  private List<Locacao> criarListaLocacoes() {

    return Arrays.asList(this.criarLocacao(), this.criarLocacao());

  }

}
