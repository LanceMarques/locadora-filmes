package com.locadora.infra.genero;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.locadora.infra.genero.exceptions.GeneroJaCadastradoException;
import com.locadora.infra.genero.exceptions.GeneroNaoEncontradoException;

@Service
public class GeneroService {

	@Autowired
	private GeneroRepository generoRepository;
	
	public List<Genero> listarTodos() {
		return generoRepository.findAll();
	}

	public Genero buscarPorId(Integer id) {
		Optional<Genero> generoOpt = generoRepository.findById(id);
		if (!generoOpt.isPresent()) {
			throw new GeneroNaoEncontradoException();
		}
		return generoOpt.get();
	}

	public Genero criar(Genero genero) {
		Optional<Genero> generoMesmoNome = buscarPorNome(genero.getNome());
		if (generoMesmoNome.isPresent()) {
			throw new GeneroJaCadastradoException();
		}
		return generoRepository.save(genero);
	}

	public Genero atualizar(Integer id, Genero genero) {
		Genero generoSalvo = buscarPorId(id);
		Optional<Genero> generoMesmoNome = buscarPorNome(genero.getNome());
		if (generoMesmoNome.isPresent() && generoMesmoNome.get().getId() != id) {
			throw new GeneroJaCadastradoException();
		}
		BeanUtils.copyProperties(genero, generoSalvo, "id");
		return generoRepository.save(generoSalvo);
	}

	public void excluir(Integer id) {
		Genero genero = buscarPorId(id);
		generoRepository.deleteById(genero.getId());
	}

	private Optional<Genero> buscarPorNome(String nome) {
		Optional<Genero> generoOpt = generoRepository.findByNome(nome);
		return generoOpt;
	}
}