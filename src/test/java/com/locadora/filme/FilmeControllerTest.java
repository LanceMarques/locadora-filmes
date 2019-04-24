package com.locadora.filme;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import com.locadora.infra.filme.Filme;
import com.locadora.infra.filme.FilmeController;
import com.locadora.infra.filme.FilmeService;
import com.locadora.infra.genero.Genero;
import com.locadora.utils.JsonUtils;

@RunWith(SpringRunner.class)
@WebMvcTest(FilmeController.class)
public class FilmeControllerTest {

  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private FilmeService filmeService;
  
  @Test
  public void testListarTodos() throws Exception {
    final List<Filme> filmes = this.criarListaFilmes();
    
    when(this.filmeService.listarTodos()).thenReturn(filmes);
    
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get("/filmes")
          .accept(MediaType.APPLICATION_JSON);
    
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isOk())
      .andExpect(content().json(JsonUtils.objetoParaJson(filmes)))
      .andReturn();
    
    verify(this.filmeService,times(1)).listarTodos();
  }

  @Test
  public void testBuscarPorId() throws Exception {
    
    final Filme filme = this.criarFilme();
    
    when(this.filmeService.buscarPorId(1))
      .thenReturn(filme);
    
    RequestBuilder requestBuilder = 
        MockMvcRequestBuilders.get("/filmes/1")
          .accept(MediaType.APPLICATION_JSON);
    
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isOk())
      .andExpect(content().json(JsonUtils.objetoParaJson(filme)));
    
    verify(this.filmeService,times(1)).buscarPorId(1);
  }

  @Test
  public void testCriar() throws Exception {
    
    final Filme filme = this.criarFilme();
    
    when(this.filmeService.criar(filme))
      .thenReturn(filme);
    
    final RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/filmes")
        .accept(MediaType.APPLICATION_JSON)
        .content(JsonUtils.objetoParaJson(filme))
        .contentType(MediaType.APPLICATION_JSON);
    
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isCreated())
      .andExpect(content().json(JsonUtils.objetoParaJson(filme)))
      .andReturn();
    
    verify(this.filmeService,times(1)).criar(filme);
  }

  @Test
  public void testAtualizar() throws Exception {
        
    final Filme filme = this.criarFilme();
    
    when(this.filmeService.atualizar(1, filme))
      .thenReturn(filme);
    
    final RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/filmes/1")
        .content(JsonUtils.objetoParaJson(filme))
        .contentType(MediaType.APPLICATION_JSON);
    
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isNoContent())
      .andReturn();
        
    verify(this.filmeService,times(1)).atualizar(1, filme);
  }

  @Test
  public void testExcluir() throws Exception {
        
    final RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/filmes/1");
    
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isNoContent())
      .andReturn();
    
    verify(this.filmeService,times(1)).excluir(1);
  }

  private List<Filme> criarListaFilmes(){
    return Arrays.asList(
          this.criarFilme(),
          this.criarFilme()
        );        
  }
  
  private Filme criarFilme() {
    final Genero genero = new Genero(1, "terror");
    return new Filme(1, "matrix", 120, 10, "sinopse", "nome Diretor", 5.0 , genero); 
  }
  
}
