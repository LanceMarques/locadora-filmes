package com.locadora.cliente;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.locadora.infra.cliente.Cliente;
import com.locadora.infra.cliente.ClienteController;
import com.locadora.infra.cliente.ClienteService;
import com.locadora.infra.cliente.Endereco;
import com.locadora.utils.JsonUtils;

@RunWith(SpringRunner.class)
@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private ClienteService clienteService;
  
  @Before
  public void setUp() throws Exception {}

  @Test
  public void testListarTodos() throws Exception {    
    final List<Cliente> clientes = this.criarListaClientes();
            
    when(this.clienteService.listarTodos()).thenReturn(clientes);
    
    final RequestBuilder requestBuilder =
        MockMvcRequestBuilders
          .get("/clientes")
          .accept(MediaType.APPLICATION_JSON);
    
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isOk())
      .andExpect(content().json(JsonUtils.objetoParaJson(clientes)))
      .andReturn();

    verify(this.clienteService,times(1)).listarTodos();    
  }

  @Test
  public void testBuscarPorId() throws Exception {
    final Cliente cliente = criarCliente();
    
    when(this.clienteService.buscarPorId(1)).thenReturn(cliente);
    
    final RequestBuilder requestBuilder =
        MockMvcRequestBuilders
          .get("/clientes/1")
          .accept(MediaType.APPLICATION_JSON);
        
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isOk())
      .andExpect(content().json(JsonUtils.objetoParaJson(cliente)))
      .andReturn();
    
    verify(this.clienteService,times(1)).buscarPorId(1);
  }

  @Test
  public void testBuscarPorCpf() throws Exception {    
    final Cliente cliente = criarCliente();
    
    final String cpf = cliente.getCpf();
    
    when(this.clienteService.buscarClientePorCpf(cpf)).thenReturn(cliente);
    
    final RequestBuilder requestBuilder =
        MockMvcRequestBuilders
          .get("/clientes/cpf/"+cpf)
          .accept(MediaType.APPLICATION_JSON);
        
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isOk())
      .andExpect(content().json(JsonUtils.objetoParaJson(cliente)))
      .andReturn();
    
    verify(this.clienteService,times(1)).buscarClientePorCpf(cpf);    
  }

  @Test
  public void testCriar() throws Exception {    
    final Cliente cliente = criarCliente();
        
    when(this.clienteService.criar(cliente)).thenReturn(cliente);
    
    final RequestBuilder requestBuilder =
        MockMvcRequestBuilders
          .post("/clientes")
          .accept(MediaType.APPLICATION_JSON)
          .content(JsonUtils.objetoParaJson(cliente))
          .contentType(MediaType.APPLICATION_JSON);
        
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isCreated())
      .andExpect(content().json(JsonUtils.objetoParaJson(cliente)))
      .andReturn();
    
    verify(this.clienteService, times(1)).criar(cliente);    
  }

  @Test
  public void testAtualizar() throws Exception {    
    final Cliente cliente = criarCliente();
    
    when(this.clienteService.atualizar(1, cliente)).thenReturn(cliente);
    
    final RequestBuilder requestBuilder =
        MockMvcRequestBuilders
          .put("/clientes/1")
          .content(JsonUtils.objetoParaJson(cliente))
          .contentType(MediaType.APPLICATION_JSON);
        
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isNoContent())
      .andReturn();
    
    verify(this.clienteService,times(1)).atualizar(1, cliente);    
  }

  @Test
  public void testExcluir() throws Exception {
    final RequestBuilder requestBuilder =
        MockMvcRequestBuilders
          .delete("/clientes/1");
        
    this.mockMvc.perform(requestBuilder)
      .andExpect(status().isNoContent())
      .andReturn();
    
    verify(this.clienteService,times(1)).excluir(1);
  }

  
  private Cliente criarCliente() {
    return new Cliente(1,"043.132.390-90", "Joao das Neves", new Endereco("Gelada", "58416654", "Winterfell", "", "Westeros"));
  }
  
  private List<Cliente> criarListaClientes() {
    return Arrays.asList(
        this.criarCliente(),
        this.criarCliente()
    );
  }

}
