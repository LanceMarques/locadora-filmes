package com.locadora.infra.filme;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.locadora.infra.filme.exceptions.FilmeEstoqueIndisponivelException;
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
	
	public Filme buscarPorIdComEstoqueDisponivel(int id, int quantidadeLocada) {
		Filme filmeSalvo = buscarPorId(id);
		if(filmeSalvo.getQuantidadeEstoque()<quantidadeLocada) {
			throw new FilmeEstoqueIndisponivelException();
		}
		return filmeSalvo;
	}
	
	public Filme buscarReduzirEstoque(int filmeId,int quantidadeLocada) {
		Filme filmeSalvo = this.buscarPorIdComEstoqueDisponivel(filmeId,quantidadeLocada);
		int estoqueDisponivel = filmeSalvo.getQuantidadeEstoque()-quantidadeLocada;
		filmeSalvo.setQuantidadeEstoque(estoqueDisponivel);
		return filmeSalvo;
	}
	
	public Filme buscarAcrescentarEstoque(int filmeId,int quantidadeDevolvida) {
		Filme filmeSalvo = this.buscarPorId(filmeId);
		int estoqueDisponivel = filmeSalvo.getQuantidadeEstoque()+quantidadeDevolvida;
		filmeSalvo.setQuantidadeEstoque(estoqueDisponivel);
		return filmeSalvo;
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
		Filme filmesalvo = buscarPorId(id);
		BeanUtils.copyProperties(filme, filmesalvo,"id");
		return this.filmeRepository.save(filmesalvo);
	}
	
	public void excluir(Integer id) {
		final Filme filme = buscarPorId(id);
		this.filmeRepository.deleteById(filme.getId());
	}

	public void salvarTodos(List<Filme> filmes) {
		this.filmeRepository.saveAll(filmes);
	}
	
}