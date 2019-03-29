package com.locadora.infra.locacao;

import java.util.ArrayList;
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

import net.bytebuddy.asm.Advice.Return;

@Service
public class LocacaoService {

	@Autowired
	private LocacaoRepository locacaoRepository;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private LocacaoTemFilmeService locacaoTemFilmeService;
	
	public List<Locacao> listarTodos(){
		return this.locacaoRepository.findAll();
	}
	
	public Locacao buscarPorId(Integer id) {
		Optional<Locacao> locacaoOpt = locacaoRepository.findById(id);
		if(!locacaoOpt.isPresent()) {
			throw new LocacaoNaoEncontradaException();
		}
		return locacaoOpt.get();
	}
	
	public List<Locacao> buscarPorCliente(Cliente cliente) {
		return this.locacaoRepository.findByCliente(cliente);
	}
	
	public Locacao criar(Locacao locacao) {
		
		List<LocacaoTemFilme> filmesAssociados = locacao.getFilmes();
		
		final Cliente clienteValido = buscarClienteValido(locacao);
				
		locacao.setCliente(clienteValido);
		locacao.setFilmes(null);
		
		Locacao locacaoSalva = this.locacaoRepository.save(locacao);
		
		
		 filmesAssociados = locacaoTemFilmeService.associarFilmes(filmesAssociados,
		 locacaoSalva);
		 
		 locacaoSalva.setFilmes(filmesAssociados);
		 
		 return atualizar(locacaoSalva.getId(), locacaoSalva);
		
	}
		
	public Locacao atualizar(Integer id, Locacao locacao) {
		/*
		Locacao locacaoSalva = buscarPorId(id);
		locacaoSalva.limparFilmes();
		return locacaoRepository.save(locacaoSalva);
		locacaoSalva.atualizaFilmes(filmes);
		
		return locacaoRepository.save(locacaoSalva);*/
		Locacao locacaoSalva = buscarPorId(id);
		List<LocacaoTemFilme> filmes = locacao.getFilmes();
		BeanUtils.copyProperties(locacao, locacaoSalva,"id");
		locacaoSalva.limparFilmes();
		locacaoRepository.save(locacaoSalva);
		filmes = locacaoTemFilmeService.associarFilmes(filmes, locacaoSalva);
		locacaoSalva.atualizaFilmes(filmes);
		return locacaoRepository.save(locacaoSalva);
	}
	
	
	public void excluir(Integer id) {
		Locacao locacaoSalva = this.buscarPorId(id);
		this.locacaoRepository.deleteById(locacaoSalva.getId());
	}
		
	public Cliente buscarClienteValido(Locacao locacao) {
		int clienteId = locacao.getCliente().getId();
		
		int filmesComCliente;
		int filmesNaLocacao;
		
		Cliente clienteSalvo = clienteService.buscarPorId(clienteId);
		List<Locacao> locacoesDoCliente = buscarPorCliente(clienteSalvo);
		
		filmesComCliente = locacaoTemFilmeService.contarFilmesNasLocacoes(locacoesDoCliente);
		filmesNaLocacao = locacaoTemFilmeService.contarFilmes(locacao.getFilmes());
			
		if(filmesComCliente+filmesNaLocacao>5) {
			System.out.println("filmes com o cliente " +filmesComCliente);
			System.out.println("filmes na locacao " +filmesNaLocacao);
			throw new LocacaoLimiteDeFilmesException();
		}
		return clienteSalvo;
	}
	
}
