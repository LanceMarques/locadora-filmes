package com.locadora.cliente;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.locadora.infra.cliente.Cliente;
import com.locadora.infra.cliente.ClienteRepository;
import com.locadora.infra.cliente.ClienteService;
import com.locadora.infra.cliente.Endereco;
import com.locadora.infra.cliente.exceptions.ClienteNaoEncontradoException;
import com.locadora.infra.cliente.exceptions.CpfJaCadastradoException;

@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceTest {

  @InjectMocks
  private ClienteService clienteService;

  @Mock
  private ClienteRepository clienteRepository;

  @Test
  public void testListarTodos() {

    when(this.clienteRepository.findAll()).thenReturn(this.criarListaDeClientes());
    final List<Cliente> clientes = this.clienteService.listarTodos();

    assertThat(clientes, hasSize(3));

  }

  @Test
  public void testBuscarPorId() {
    final Endereco endereco1 = new Endereco("Sergipe", "58415479", "cruzeiro", "", "campina");
    final Cliente clienteSalvo = this.criarCliente(1, "957.101.910-00", "Lance Marques", endereco1);

    when(this.clienteRepository.findById(clienteSalvo.getId()))
        .thenReturn(Optional.of(clienteSalvo));

    final Cliente clienteBuscado = this.clienteService.buscarPorId(clienteSalvo.getId());

    assertThat(clienteBuscado, equalTo(clienteSalvo));

  }


  @Test
  public void testBuscarPorId_ClienteNaoEncontradoException() {

    when(this.clienteRepository.findById(1)).thenReturn(Optional.ofNullable(null));

    try {
      this.clienteService.buscarPorId(1);
    } catch (Exception e) {
      assertThat(e, instanceOf(ClienteNaoEncontradoException.class));
    }

  }


  @Test
  public void testBuscarPorCpf() {
    final Endereco endereco1 = this.criarEndereco("Sergipe", "58415479", "cruzeiro", "", "campina");
    final Cliente clienteSalvo = this.criarCliente(1, "957.101.910-00", "Lance Marques", endereco1);

    when(this.clienteRepository.findByCpf(clienteSalvo.getCpf()))
        .thenReturn(Optional.of(clienteSalvo));

    final Cliente clienteBuscado = this.clienteService.buscarPorCpf(clienteSalvo.getCpf()).get();

    assertThat(clienteBuscado, equalTo(clienteSalvo));
  }

  @Test
  public void testBuscarClientePorCpf() {
    final Endereco endereco1 = this.criarEndereco("Sergipe", "58415479", "cruzeiro", "", "campina");
    final Cliente clienteSalvo = this.criarCliente(1, "957.101.910-00", "Lance Marques", endereco1);

    when(this.clienteRepository.findByCpf(clienteSalvo.getCpf()))
        .thenReturn(Optional.of(clienteSalvo));

    final Cliente clienteBuscado = this.clienteService.buscarClientePorCpf(clienteSalvo.getCpf());

    assertThat(clienteBuscado, equalTo(clienteSalvo));

  }

  @Test
  public void testBuscarClientePorCpf_ClienteNaoEncontradoException() {
    when(this.clienteRepository.findByCpf("957.101.910-00")).thenReturn(Optional.ofNullable(null));

    try {
      this.clienteService.buscarClientePorCpf("957.101.910-00");
    } catch (Exception e) {
      assertThat(e, instanceOf(ClienteNaoEncontradoException.class));
    }
  }

  @Test
  public void testCriar() {
    Endereco endereco1 = this.criarEndereco("Sergipe", "58415479", "cruzeiro", "", "campina");
    Cliente clienteASalvar = this.criarCliente(1, "957.101.910-00", "Lance Marques", endereco1);

    when(this.clienteRepository.save(clienteASalvar)).thenReturn(clienteASalvar);

    Cliente clienteSalvo = this.clienteService.criar(clienteASalvar);

    assertThat(clienteSalvo, equalTo(clienteASalvar));

  }

  @Test
  public void testCriar_CpfJaCadastradoException() {
    Endereco endereco1 = this.criarEndereco("Sergipe", "58415479", "cruzeiro", "", "campina");
    Cliente clienteASalvar = this.criarCliente(1, "957.101.910-00", "Lance Marques", endereco1);
    Cliente clienteSalvo = this.criarCliente(1, "957.101.910-00", "Luis Marques", endereco1);

    when(this.clienteRepository.findByCpf(clienteASalvar.getCpf()))
        .thenReturn(Optional.of(clienteSalvo));

    try {
      this.clienteService.criar(clienteASalvar);
    } catch (Exception e) {
      assertThat(e, instanceOf(CpfJaCadastradoException.class));
    }

  }

  @Test
  public void testAtualizar() {
    Endereco endereco1 = this.criarEndereco("Sergipe", "58415479", "cruzeiro", "", "campina");
    Cliente clienteSalvo = this.criarCliente(1, "957.101.910-00", "Lance Marques", endereco1);
    Cliente clienteAAtualizar = this.criarCliente(1, "957.101.910-00", "Luis Marques", endereco1);

    when(this.clienteRepository.findById(clienteAAtualizar.getId()))
        .thenReturn(Optional.of(clienteSalvo));

    when(this.clienteRepository.findByCpf(clienteAAtualizar.getCpf()))
        .thenReturn(Optional.of(clienteSalvo));

    when(this.clienteRepository.save(clienteSalvo)).thenReturn(clienteSalvo);
    Cliente clienteAtualizado =
        this.clienteService.atualizar(clienteAAtualizar.getId(), clienteAAtualizar);

    assertThat(clienteAtualizado, equalTo(clienteAAtualizar));
  }

  @Test
  public void testAtualizar_CpfJaCadastradoException() {
    Endereco endereco1 = this.criarEndereco("Sergipe", "58415479", "cruzeiro", "", "campina");
    Cliente clienteSalvo = this.criarCliente(1, "957.101.910-02", "Lance Marques", endereco1);
    Cliente clienteMesmoCpf = this.criarCliente(2, "957.101.910-00", "Luis Marques", endereco1);
    Cliente clienteAAtualizar = this.criarCliente(1, "957.101.910-00", "Luis Marques", endereco1);

    when(this.clienteRepository.findById(clienteAAtualizar.getId()))
        .thenReturn(Optional.of(clienteSalvo));

    when(this.clienteRepository.findByCpf(clienteAAtualizar.getCpf()))
        .thenReturn(Optional.of(clienteMesmoCpf));

    try {
          this.clienteService.atualizar(clienteAAtualizar.getId(), clienteAAtualizar);

    } catch (Exception e) {
      assertThat(e, instanceOf(CpfJaCadastradoException.class));
    }
  }

  @Test
  public void testExcluir() {
    Cliente clienteSalvo = this.criarCliente(1, "957.101.910-02", "Lance Marques", null);
    when(this.clienteRepository.findById(1)).thenReturn(Optional.of(clienteSalvo));
    this.clienteService.excluir(1);
    verify(this.clienteRepository,times(1)).deleteById(1);
  }

  @Test
  public void testFormatarCpf() {
    String cpf = "18854080055";
    String cpfFormatado = this.clienteService.formatarCpf(cpf);
    
    assertThat(cpfFormatado, equalTo("188.540.800-55"));
  }

  
  private List<Cliente> criarListaDeClientes() {

    Endereco endereco1 = this.criarEndereco("Sergipe", "58415479", "cruzeiro", "", "campina");

    return Arrays.asList(new Cliente(1, "957.101.910-00", "Lance Marques", endereco1),
        new Cliente(2, "188.540.800-55", "Luis Lance", endereco1),
        new Cliente(3, "033.041.450-01", "Lancelote Marques", endereco1));
  }

  private Cliente criarCliente(Integer id, String cpf, String nome, Endereco endereco) {
    return new Cliente(id, cpf, nome, endereco);
  }

  private Endereco criarEndereco(String rua, String cep, String bairro, String complemento,
      String cidade) {
    return new Endereco(rua, cep, bairro, complemento, cidade);
  }

}
