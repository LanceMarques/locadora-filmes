package com.locadora.infra.filme;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.locadora.infra.filme.exceptions.FilmeNaoEncontradoException;
import com.locadora.infra.genero.Genero;

@Service
public class FilmeService {

	@Autowired
	private FilmeRepository filmeRepository;
	
	public List<Filme> listarTodos(){
		return filmeRepository.findAll();
	}
	
	public Filme buscarPorId(Integer id) {
		final Optional<Filme> filmeOpt = filmeRepository.findById(id);
		if(!filmeOpt.isPresent()) {
			throw new FilmeNaoEncontradoException();
		}
		return filmeOpt.get();
	}
	
	public Filme criar(Filme filme) {
		return filmeRepository.save(filme);
	}

	public Filme atualizar(Integer id, @Valid Filme filme) {
		final Filme filmesalvo = buscarPorId(id);
		BeanUtils.copyProperties(filme, filmesalvo,"id");
		return filmeRepository.save(filmesalvo);
	}
	
	public void excluir(Integer id) {
		final Filme filme = buscarPorId(id);
		filmeRepository.deleteById(filme.getId());
	}

	public List<Filme> findByGenero(Genero genero) {
		return filmeRepository.findByGenero(genero);
	}
	
}