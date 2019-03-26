package com.locadora.infra.locacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.locadora.infra.locacaoTemFilme.LocacaoTemFilmeService;

@Service
public class LocacaoService {

	@Autowired
	private LocacaoRepository locacaoRepository;
	@Autowired
	private LocacaoTemFilmeService locacaoTemFilmeService;
	
	
	public Locacao criar(Locacao locacao) {
		locacaoTemFilmeService.calculaTotalDiaria(locacao.getFilmes());
		return this.locacaoRepository.save(locacao);
	}
	
}
