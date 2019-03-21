package com.locadora.infra.genero;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.locadora.infra.genero.exceptions.GeneroJaCadastradoException;
import com.locadora.infra.genero.exceptions.GeneroNaoEncontradoException;

@Service
public class GeneroService {

	@Autowired
	private GeneroRepository generoRepository;
	
	public List<Genero> listarTodos(){
		return generoRepository.findAll();
	}

	public Genero buscarPorId(int id) {
		Optional<Genero> generoOpt = generoRepository.findById(id);
		if(!generoOpt.isPresent()) {
			throw new GeneroNaoEncontradoException();
		}
		return generoOpt.get();
	}
	
	public Genero criar(Genero genero) {
		Optional<Genero> generoOpt = buscarPorNome(genero.getNome()); 
		if(generoOpt.isPresent()) {
			throw new GeneroJaCadastradoException();
		}
		return generoRepository.save(genero);
	}

	public Genero atualizar(int id, Genero genero) {
		Optional<Genero> generoOpt = buscarPorNome(genero.getNome());
		if(generoOpt.isPresent()&&generoOpt.get().getId()!=id) {
			throw new GeneroJaCadastradoException();
		}
		Genero generoSalvo = buscarPorId(id);			
		BeanUtils.copyProperties(genero, generoSalvo, "id");
		return generoRepository.save(generoSalvo);
	}

	public void excluir(int id) {
		Genero genero = buscarPorId(id);
		generoRepository.deleteById(genero.getId());
	}
	
	private Optional<Genero> buscarPorNome(String nome){
		return generoRepository.findByNome(nome);
	}
}