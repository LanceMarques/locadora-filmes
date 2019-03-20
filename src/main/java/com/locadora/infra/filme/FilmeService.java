package com.locadora.infra.filme;

import java.util.List;
import java.util.Optional;

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
	
	public Filme BuscarPorId(int id) {
		Optional<Filme> filmeOpt = filmeRepository.findById(id);
		if(!filmeOpt.isPresent()) {
			throw new FilmeNaoEncontradoException();
		}
		return filmeOpt.get();
	}
	
	public Filme criar(Filme filme) {
		return filmeRepository.save(filme);
	}
	
}
