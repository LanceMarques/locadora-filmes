package com.locadora.genero;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.locadora.infra.genero.Genero;
import com.locadora.infra.genero.GeneroController;
import com.locadora.infra.genero.GeneroService;

@RunWith(SpringRunner.class)
@WebMvcTest(GeneroController.class)
public class GeneroControllerTest {

  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private GeneroService generoService;
  
  @Before
  public void setUp() throws Exception {}
    
  @Test
  public void testListarTodos() throws Exception {
    List<Genero> generos = Arrays.asList(
          new Genero(1, "terror"),
          new Genero(2,"aventura")
        );
        
    when(this.generoService.listarTodos()).thenReturn(generos);
    
    RequestBuilder request = MockMvcRequestBuilders.get("/generos")
        .accept(MediaType.APPLICATION_JSON);
        
    MvcResult andReturn = mockMvc.perform(request)
          .andExpect(status().isOk())
          .andExpect(content().json(
                "[{id:1,nome:terror},"
              + "{id:2,nome:aventura}]"              
              ))
          .andReturn();
    
  }

  @Test
  public void testBuscarPorId() throws Exception {
    Genero genero = new Genero(1, "terror");
    
    when(this.generoService.buscarPorId(1)).thenReturn(genero);
    
    RequestBuilder request = MockMvcRequestBuilders.get("/generos/1")
        .accept(MediaType.APPLICATION_JSON);
    
    MvcResult result = mockMvc.perform(request)
      .andExpect(status().isOk())
      .andExpect(content().json("{id:1,nome:terror}"))
      .andReturn();
      
    
  }

  @Test
  public void testCriar() throws Exception {
        
    when(this.generoService.criar(new Genero())).thenReturn(new Genero());
    
    RequestBuilder request = MockMvcRequestBuilders.post("/generos")
        .accept(MediaType.APPLICATION_JSON)
        .content("{\"id\":1,\"nome\":\"terror\"}")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ;
    
    MvcResult result = mockMvc.perform(request)
      .andExpect(status().isCreated())
      .andExpect(content().json("{\"id\":1,\"nome\":\"terror\"}"))
      .andReturn();

  }

  @Test
  public void testAtualizar() {

  }

  @Test
  public void testExcluir() {

  }

}
