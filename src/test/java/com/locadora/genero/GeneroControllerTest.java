package com.locadora.genero;

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
import com.locadora.infra.genero.Genero;
import com.locadora.infra.genero.GeneroController;
import com.locadora.infra.genero.GeneroService;
import com.locadora.utils.JsonUtils;

@RunWith(SpringRunner.class)
@WebMvcTest(GeneroController.class)
public class GeneroControllerTest {

  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private GeneroService generoService;
  
  @Test
  public void testListarTodos() throws Exception {
    final List<Genero> generos = Arrays.asList(
          new Genero(1, "terror"),
          new Genero(2,"aventura")
        );
        
    when(this.generoService.listarTodos()).thenReturn(generos);
    
    final RequestBuilder request = MockMvcRequestBuilders.get("/generos")
        .accept(MediaType.APPLICATION_JSON);
        
    mockMvc.perform(request)
          .andExpect(status().isOk())
          .andExpect(content().json(JsonUtils.objetoParaJson(generos)))
          .andReturn();
    
    verify(this.generoService,times(1)).listarTodos();
  }

  @Test
  public void testBuscarPorId() throws Exception {
    Genero genero = new Genero(1, "terror");
    
    when(this.generoService.buscarPorId(1)).thenReturn(genero);
    
    final RequestBuilder request = 
        MockMvcRequestBuilders
          .get("/generos/1")
          .accept(MediaType.APPLICATION_JSON);
    
    mockMvc.perform(request)
      .andExpect(status().isOk())
      .andExpect(content().json(JsonUtils.objetoParaJson(genero)))
      .andReturn();
      
    verify(this.generoService,times(1)).buscarPorId(1);
  }

  @Test
  public void testCriar() throws Exception {
        
    final Genero genero = new Genero(1, "terror");
        
    
    when(this.generoService.criar(genero)).thenReturn(genero);
    
    final RequestBuilder request = MockMvcRequestBuilders.post("/generos")
        .accept(MediaType.APPLICATION_JSON)
        .content(JsonUtils.objetoParaJson(genero))
        .contentType(MediaType.APPLICATION_JSON);
    
    mockMvc.perform(request)
      .andExpect(status().isCreated())
      .andExpect(content().json(JsonUtils.objetoParaJson(genero)))
      .andReturn();
    
    verify(this.generoService,times(1)).criar(genero);
  }

  @Test
  public void testAtualizar() throws Exception {
    
    final Genero genero = new Genero(1,"aventura");
    
    final RequestBuilder request = MockMvcRequestBuilders.put("/generos/1")
        .content(JsonUtils.objetoParaJson(genero))
        .contentType(MediaType.APPLICATION_JSON);
    
    mockMvc.perform(request)
      .andExpect(status().isNoContent())
      .andReturn();

    verify(this.generoService,times(1)).atualizar(1, genero);
  }

  @Test
  public void testExcluir() throws Exception {
    final Integer generoID = 1;
    
    final RequestBuilder request = MockMvcRequestBuilders.delete("/generos/1");
    
    mockMvc.perform(request)
      .andExpect(status().isNoContent())
      .andReturn();
    
    verify(this.generoService,times(1)).excluir(generoID);
  }

}
