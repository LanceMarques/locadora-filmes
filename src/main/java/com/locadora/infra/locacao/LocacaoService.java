package com.locadora.infra.locacao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.locadora.infra.cliente.Cliente;
import com.locadora.infra.cliente.ClienteService;
import com.locadora.infra.locacao.exceptions.LocacaoLimiteDeFilmesException;
import com.locadora.infra.locacao.exceptions.LocacaoNaoEncontradaException;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilme;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilmeService;

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

	public List<Locacao> buscarPorCliente(Cliente cliente) {
		return this.locacaoRepository.findByCliente(cliente);
	}

	public Locacao criar(Locacao locacao) {

		List<LocacaoTemFilme> filmesLocados = locacao.getFilmes();

		final Cliente clienteValido = buscarClienteValido(locacao);

		locacao.setCliente(clienteValido);
		locacao.setFilmes(null);

		Locacao locacaoSalva = this.locacaoRepository.save(locacao);

		filmesLocados = locacaoTemFilmeService.associarFilmes(filmesLocados, locacaoSalva);

		locacaoSalva.setFilmes(filmesLocados);

		return atualizar(locacaoSalva.getId(), locacaoSalva);

	}

	public Locacao atualizar(Integer id, Locacao locacao) {
		Locacao locacaoSalva = buscarPorId(id);
		BeanUtils.copyProperties(locacao, locacaoSalva, "id");
		return locacaoRepository.save(locacaoSalva);
	}

	public void excluir(Integer id) {
		Locacao locacaoSalva = this.buscarPorId(id);
		List<LocacaoTemFilme> filmesDevolvidos = locacaoSalva.getFilmes();
		this.locacaoTemFilmeService.devolverAoEstoque(filmesDevolvidos);
		this.locacaoRepository.deleteById(locacaoSalva.getId());
	}

	public Cliente buscarClienteValido(Locacao locacao) {
		int clienteId = locacao.getCliente().getId();
		
		int filmesNaLocacao;
		int filmesComCliente;

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
