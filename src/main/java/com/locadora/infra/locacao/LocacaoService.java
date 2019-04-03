package com.locadora.infra.locacao;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.locadora.infra.cliente.Cliente;
import com.locadora.infra.cliente.ClienteService;
import com.locadora.infra.enums.StatusLocacao;
import com.locadora.infra.locacao.exceptions.LocacaoLimiteDeFilmesException;
import com.locadora.infra.locacao.exceptions.LocacaoNaoEncontradaException;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilme;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilmeService;
import com.locadora.utils.DataUtils;

@Service
public class LocacaoService {

  @Autowired
  private LocacaoRepository locacaoRepository;
  @Autowired
  private ClienteService clienteService;
  @Autowired
  private LocacaoTemFilmeService locacaoTemFilmeService;

  public List<Locacao> listarTodos() {
    return this.locacaoRepository.findAll();
  }

  public Locacao buscarPorId(Integer id) {
    Optional<Locacao> locacaoOpt = locacaoRepository.findById(id);
    if (!locacaoOpt.isPresent()) {
      throw new LocacaoNaoEncontradaException();
    }
    return locacaoOpt.get();
  }
  
  public List<LocacaoTemFilme> listarFilmes(Integer id) {
    Locacao locacaoSalva = this.buscarPorId(id);
    return locacaoSalva.getFilmes();
  }

  public List<Locacao> listarPorStatus(String statusLocacao) {
    StatusLocacao status = StatusLocacao.valueOf(statusLocacao);
    return this.locacaoRepository.findByStatus(status);
  }
  
  public List<Locacao> buscarPorCliente(Cliente cliente) {
    return this.locacaoRepository.findByCliente(cliente);
  }

  public Locacao criar(Locacao locacao) {

    Locacao locacaoSalva = locarFilmes(locacao);
    return this.locacaoRepository.save(locacaoSalva);

  }

  public Locacao atualizar(Integer id, Locacao locacao) {
    Locacao locacaoSalva = buscarPorId(id);
    BeanUtils.copyProperties(locacao, locacaoSalva, "id");
    return locacaoRepository.save(locacaoSalva);
  }
  
  public void devolverLocacao(Integer id) {
    Locacao locacaoSalva = this.buscarPorId(id);
    this.devolverFilmes(locacaoSalva);
    locacaoSalva.setDataDevolucao(DataUtils.gerarDataAtual());
    locacaoSalva.setStatus(StatusLocacao.FINALIZADO);
    locacaoSalva.setValorTotal(calcularValorTotal(locacaoSalva));
    this.locacaoRepository.save(locacaoSalva);
  }
  
  public Double calcularValorTotal(Locacao locacao) {
    Long intervaloMilis = locacao.getDataDevolucao().getTime()-locacao.getDataRealizacao().getTime();
    Long intervaloDias =1+ intervaloMilis/(1000*60*60*24);
    double totalFilmes=0;
    for (LocacaoTemFilme locacaoTemfilme: locacao.getFilmes()) {
      totalFilmes+=locacaoTemfilme.getValorTotalDaDiaria();
    }
    Double valorTotal =totalFilmes*intervaloDias;
    return valorTotal;
  }
  
  public void excluir(Integer id) {
    Locacao locacaoSalva = this.buscarPorId(id);
    devolverFilmes(locacaoSalva);
    this.locacaoRepository.deleteById(locacaoSalva.getId());
  }

  private Locacao locarFilmes(Locacao locacao) {
    List<LocacaoTemFilme> filmesLocados = locacao.getFilmes();  
    final Cliente clienteValido = buscarClienteValido(locacao);

    locacao.setFilmes(null);
    locacao.setCliente(clienteValido);
    locacao.setStatus(StatusLocacao.ABERTO);
    locacao.setDataRealizacao(DataUtils.gerarDataAtual());
    
    Locacao locacaoSalva = this.locacaoRepository.save(locacao);

    filmesLocados = locacaoTemFilmeService.associarFilmes(filmesLocados, locacaoSalva);

    locacaoSalva.setFilmes(filmesLocados);
    return locacaoSalva;
  }
  
  private void devolverFilmes(Locacao locacaoSalva) {
    List<LocacaoTemFilme> filmesDevolvidos = locacaoSalva.getFilmes();
    this.locacaoTemFilmeService.devolverAoEstoque(filmesDevolvidos);
  }
  
  public Cliente buscarClienteValido(Locacao locacao) {
    Integer clienteId = locacao.getCliente().getId();

    Integer filmesNaLocacao;
    Integer filmesComCliente;

    Cliente clienteSalvo = clienteService.buscarPorId(clienteId);
    List<Locacao> locacoesDoCliente = buscarPorCliente(clienteSalvo);

    filmesNaLocacao = locacaoTemFilmeService.contarFilmes(locacao.getFilmes());
    filmesComCliente = locacaoTemFilmeService.contarFilmesNasLocacoes(locacoesDoCliente);

    if (filmesComCliente + filmesNaLocacao > 5) {
      throw new LocacaoLimiteDeFilmesException();
    }
    return clienteSalvo;
  }

}
