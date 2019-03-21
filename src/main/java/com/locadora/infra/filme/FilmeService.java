package com.locadora.infra.filme;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.locadora.infra.filme.exceptions.FilmeNaoEncontradoException;

@Service
public class FilmeService {

	@Autowired
	private FilmeRepository filmeRepository;
	
	public List<Filme> listarTodos(){
		return filmeRepository.findAll();
	}
	
	public Filme buscarPorId(int id) {
		Optional<Filme> filmeOpt = filmeRepository.findById(id);
		if(!filmeOpt.isPresent()) {
			throw new FilmeNaoEncontradoException();
		}
		return filmeOpt.get();
	}
	
	public Filme criar(Filme filme) {
		return filmeRepository.save(filme);
	}

	public Filme atualizar(int id, @Valid Filme filme) {
		Filme filmesalvo = buscarPorId(id);
		BeanUtils.copyProperties(filme, filmesalvo,"id");
		return filmeRepository.save(filmesalvo);
	}
	
	public void excluir(int id) {
		Filme filme = buscarPorId(id);
		filmeRepository.deleteById(filme.getId());
	}
	
}