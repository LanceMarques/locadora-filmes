package com.locadora.infra.locacao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.locadora.infra.locacaoTemFilme.LocacaoTemFilme;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilmeService;

@Service
public class LocacaoService {

	@Autowired
	private LocacaoRepository locacaoRepository;
	@Autowired
	private LocacaoTemFilmeService locacaoTemFilmeService;
	
	public Locacao buscarPorId(Integer id) {
		Optional<Locacao> locacaoOpt = locacaoRepository.findById(id);
		return locacaoOpt.get();
	}
	
	public Locacao criar(Locacao locacao) {
		List<LocacaoTemFilme> filmesLocados = locacao.getFilmes();
		locacao.setFilmes(null);

		Locacao locacaoSalva = this.locacaoRepository.save(locacao);
	
		filmesLocados = locacaoTemFilmeService.associarFilmes(filmesLocados, locacaoSalva);
		
		locacaoSalva.setFilmes(filmesLocados);
		
		return atualizar(locacaoSalva.getId(), locacaoSalva);
	}
	
	public Locacao atualizar(Integer id, Locacao locacao) {
		Locacao locacaoSalva = buscarPorId(id);
		BeanUtils.copyProperties(locacao, locacaoSalva,"id");
		return locacaoRepository.save(locacaoSalva);
	}
	
}
