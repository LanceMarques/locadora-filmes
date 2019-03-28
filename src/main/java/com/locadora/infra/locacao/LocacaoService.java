package com.locadora.infra.locacao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.locadora.infra.locacao.exceptions.LocacaoNaoEncontradaException;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilme;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilmeService;

@Service
public class LocacaoService {

	@Autowired
	private LocacaoRepository locacaoRepository;
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
	
	public Locacao criar(Locacao locacao) {
		List<LocacaoTemFilme> filmesAssociados = locacao.getFilmes();
		locacao.setFilmes(null);
		Locacao locacaoSalva = this.locacaoRepository.save(locacao);
	
		filmesAssociados = locacaoTemFilmeService.associarFilmes(filmesAssociados, locacaoSalva);
		
		locacaoSalva.setFilmes(filmesAssociados);
		
		return atualizar(locacaoSalva.getId(), locacaoSalva);
	}
	
	public Locacao atualizar(Integer id, Locacao locacao) {
		Locacao locacaoSalva = buscarPorId(id);
		BeanUtils.copyProperties(locacao, locacaoSalva,"id");
		return locacaoRepository.save(locacaoSalva);
	}
	
	public void excluir(Integer id) {
		Locacao locacaoSalva = this.buscarPorId(id);
		this.locacaoRepository.deleteById(locacaoSalva.getId());
	}
	
}
