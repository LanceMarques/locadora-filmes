package com.locadora.infra.filme;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.locadora.infra.filme.exceptions.FilmeNaoEncontradoException;
import com.locadora.infra.genero.Genero;
import com.locadora.infra.genero.GeneroService;

@Service
public class FilmeService {

	@Autowired
	private FilmeRepository filmeRepository;
	@Autowired
	private GeneroService generoService;
	
	public List<Filme> listarTodos(){
		return this.filmeRepository.findAll();
	}
	
	public Filme buscarPorId(Integer id) {
		final Optional<Filme> filmeOpt = this.filmeRepository.findById(id);
		if(!filmeOpt.isPresent()) {
			throw new FilmeNaoEncontradoException();
		}
		return filmeOpt.get();
	}
	
	public List<Filme> buscarPorGenero(Genero genero) {
		return this.filmeRepository.findByGenero(genero);
	}
	
	public Filme criar(Filme filme) {
		Integer generoId=filme.getGenero().getId();
		Genero generoSalvo = generoService.buscarPorId(generoId);
		
		filme.setGenero(generoSalvo);
		return this.filmeRepository.save(filme);
	}

	public Filme atualizar(Integer id, Filme filme) {
		final Filme filmesalvo = buscarPorId(id);
		BeanUtils.copyProperties(filme, filmesalvo,"id");
		return this.filmeRepository.save(filmesalvo);
	}
	
	public void excluir(Integer id) {
		final Filme filme = buscarPorId(id);
		this.filmeRepository.deleteById(filme.getId());
	}
	
	public void atualizaEstoqueSaida(int quantidadeLocada, Filme filme) {
		atualizaEstoque(-quantidadeLocada, filme);
	}
	
	public void atualizaEstoqueEntrada(int quantidadeDevolvida, Filme filme) {
		atualizaEstoque(quantidadeDevolvida, filme);
	}
	
	public void atualizaEstoque(int quantidade, Filme filme) {
		int estoqueDisponivel = filme.getQuantidadeEstoque();
		filme.setQuantidadeEstoque(estoqueDisponivel+quantidade);
		atualizar(filme.getId(), filme);
	}

}