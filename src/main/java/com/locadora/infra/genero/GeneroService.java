package com.locadora.infra.genero;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.locadora.infra.filme.Filme;
import com.locadora.infra.filme.FilmeService;
import com.locadora.infra.genero.exceptions.FilmeAssociadoException;
import com.locadora.infra.genero.exceptions.GeneroJaCadastradoException;
import com.locadora.infra.genero.exceptions.GeneroNaoEncontradoException;

@Service
public class GeneroService {

	@Autowired
	private GeneroRepository generoRepository;
	
	@Autowired
	private FilmeService filmeService;
	
	public List<Genero> listarTodos() {
		return this.generoRepository.findAll();
	}

	public Genero buscarPorId(Integer id) {
		final Optional<Genero> generoOpt = this.generoRepository.findById(id);
		if (!generoOpt.isPresent()) {
			throw new GeneroNaoEncontradoException();
		}
		return generoOpt.get();
	}

	public Genero criar(Genero genero) {
		final Optional<Genero> generoMesmoNome = buscarPorNome(genero.getNome());
		if (generoMesmoNome.isPresent()) {
			throw new GeneroJaCadastradoException();
		}
		return this.generoRepository.save(genero);
	}

	public Genero atualizar(Integer id, Genero genero) {
		final Genero generoSalvo = buscarPorId(id);
		final Optional<Genero> generoMesmoNome = buscarPorNome(genero.getNome());
		if (generoMesmoNome.isPresent() && generoMesmoNome.get().getId() != id) {
			throw new GeneroJaCadastradoException();
		}
		BeanUtils.copyProperties(genero, generoSalvo, "id");
		return this.generoRepository.save(generoSalvo);
	}

	public void excluir(Integer id) {
		final Genero genero = buscarPorId(id);
		List<Filme> filmesAssociados = filmeService.buscarPorGenero(genero);
		if(filmesAssociados.isEmpty()) {
		this.generoRepository.deleteById(genero.getId());
		}else throw new FilmeAssociadoException();
	}

	private Optional<Genero> buscarPorNome(String nome) {
		final Optional<Genero> generoOpt = this.generoRepository.findByNome(nome);
		return generoOpt;
	}
}