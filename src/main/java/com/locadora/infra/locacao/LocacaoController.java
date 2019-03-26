package com.locadora.infra.locacao;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locacoes")
public class LocacaoController {
	
	@Autowired
	private LocacaoService locacaoService;
	
	@GetMapping
	public ResponseEntity<List<Locacao>> listarTodas(){
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PostMapping
	public ResponseEntity<Locacao> criar(@Valid @RequestBody Locacao locacao){
		Locacao locacaoSalva = locacaoService.criar(locacao);
		return ResponseEntity.status(HttpStatus.CREATED).body(locacaoSalva);
	}
	
}
