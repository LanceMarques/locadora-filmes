package com.locadora.infra.locacao;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locacoes")
public class LocacaoController {

  @Autowired
  private LocacaoService locacaoService;

  @GetMapping
  public ResponseEntity<List<Locacao>> listarTodas() {
    List<Locacao> locacoes = this.locacaoService.listarTodos();
    return ResponseEntity.status(HttpStatus.OK).body(locacoes);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Locacao> buscarPorId(@PathVariable Integer id) {
    Locacao locacaoSalva = this.locacaoService.buscarPorId(id);
    return ResponseEntity.status(HttpStatus.OK).body(locacaoSalva);
  }

  @PostMapping
  public ResponseEntity<Locacao> criar(@Valid @RequestBody Locacao locacao) {
    Locacao locacaoSalva = locacaoService.criar(locacao);
    return ResponseEntity.status(HttpStatus.CREATED).body(locacaoSalva);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Locacao> atualizar(@PathVariable Integer id, @RequestBody Locacao locacao) {
    Locacao locacaoAtualizada = this.locacaoService.atualizar(id, locacao);
    return ResponseEntity.status(HttpStatus.OK).body(locacaoAtualizada);
  }

  @PutMapping("/devolucao/{id}")
  public ResponseEntity<Locacao> devolverLocacao(@PathVariable Integer id){
    this.locacaoService.devolverLocacao(Integer id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<?> excluir(@PathVariable Integer id) {
    this.locacaoService.excluir(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

}
