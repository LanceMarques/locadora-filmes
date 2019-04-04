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
  
  public List<Locacao> pesquisar(LocacaoFilter filter){
    return this.locacaoRepository.filtrar(filter);
  }
  
  public Locacao buscarPorId(Integer id) {
    final Optional<Locacao> locacaoOpt = locacaoRepository.findById(id);
    if (!locacaoOpt.isPresent()) {
      throw new LocacaoNaoEncontradaException();
    }
    return locacaoOpt.get();
  }
  

  public List<Locacao> listarPorCPF(String cpf){
    final Cliente clienteSalvo = this.clienteService.buscarClientePorCpf(cpf);
    final List<Locacao> locacoes = this.listarPendenciasDoCliente(clienteSalvo);
    return locacoes;
  }

  private List<Locacao> listarPendenciasDoCliente(Cliente clienteSalvo) {
    final List<Locacao> locacoes = this.locacaoRepository.findByCliente(clienteSalvo);
    locacoes.removeIf(locacao->locacao.getStatus()==StatusLocacao.FINALIZADO);
    return locacoes;
  }
  
  public List<LocacaoTemFilme> listarFilmes(Integer id) {
    Locacao locacaoSalva = this.buscarPorId(id);
    return locacaoSalva.getFilmes();
  }
  
  public Locacao criar(Locacao locacao) {
    final Locacao locacaoSalva = locarFilmes(locacao);
    return this.locacaoRepository.save(locacaoSalva);
  }

  public Locacao atualizar(Integer id, Locacao locacao) {
    final Locacao locacaoSalva = buscarPorId(id);
    BeanUtils.copyProperties(locacao, locacaoSalva, "id");
    return locacaoRepository.save(locacaoSalva);
  }
  
  public void devolverLocacao(Integer id) {
    final Locacao locacaoSalva = this.buscarPorId(id);
    this.devolverFilmes(locacaoSalva);
    locacaoSalva.setDataDevolucao(DataUtils.gerarDataAtual());
    locacaoSalva.setStatus(StatusLocacao.FINALIZADO);
    locacaoSalva.setValorTotal(calcularValorTotal(locacaoSalva));
    this.locacaoRepository.save(locacaoSalva);
  }
  
  public Double calcularValorTotal(Locacao locacao) {
    final Long intervaloMilis = locacao.getDataDevolucao().getTime()-locacao.getDataRealizacao().getTime();
    final Long intervaloDias =1+ intervaloMilis/(1000*60*60*24);
    double totalFilmes=0;
    for (LocacaoTemFilme locacaoTemfilme: locacao.getFilmes()) {
      totalFilmes+=locacaoTemfilme.getValorTotalDaDiaria();
    }
    final Double valorTotal =totalFilmes*intervaloDias;
    return valorTotal;
  }
  
  public void excluir(Integer id) {
    final Locacao locacaoSalva = this.buscarPorId(id);
    devolverFilmes(locacaoSalva);
    this.locacaoRepository.deleteById(locacaoSalva.getId());
  }

  private Locacao locarFilmes(Locacao locacao) {
    
    List<LocacaoTemFilme> filmesLocados = this.locacaoTemFilmeService.verificaFilmes(locacao.getFilmes());
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
    final List<LocacaoTemFilme> filmesDevolvidos = locacaoSalva.getFilmes();
    this.locacaoTemFilmeService.devolverAoEstoque(filmesDevolvidos);
  }
  
  private Cliente buscarClienteValido(Locacao locacao) {
    final Integer clienteId = locacao.getCliente().getId();

    final Integer filmesNaLocacao;
    final Integer filmesComCliente;

    final Cliente clienteSalvo = clienteService.buscarPorId(clienteId);
    final List<Locacao> locacoesDoCliente = this.listarPendenciasDoCliente(clienteSalvo); 

    filmesNaLocacao = locacaoTemFilmeService.contarFilmes(locacao.getFilmes());
    filmesComCliente = locacaoTemFilmeService.contarFilmesNasLocacoes(locacoesDoCliente);

    if (filmesComCliente + filmesNaLocacao > 5) {
      throw new LocacaoLimiteDeFilmesException();
    }
    return clienteSalvo;
  }

}