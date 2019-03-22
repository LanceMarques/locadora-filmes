package com.locadora.infra.cliente;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import com.locadora.event.RecursoCriadoEvent;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	ClienteService clienteService;

	@Autowired
	ApplicationEventPublisher publisher;

	@GetMapping
	public ResponseEntity<List<Cliente>> listarTodos() {
		final List<Cliente> clientes = clienteService.listarTodos();
		return ResponseEntity.status(HttpStatus.OK).body(clientes);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscarPorId(@PathVariable("id") Integer id) {
		final Cliente cliente = clienteService.buscarPorId(id);
		return ResponseEntity.status(HttpStatus.OK).body(cliente);

	}

	@GetMapping("cpf/{cpf}")
	public ResponseEntity<Cliente> buscarPorCpf(@PathVariable("cpf") String cpf) {
		final Cliente cliente = clienteService.buscarClientePorCpf(cpf);
		return ResponseEntity.status(HttpStatus.OK).body(cliente);
	}

	@PostMapping
	public ResponseEntity<Cliente> criar(@RequestBody @Valid Cliente cliente, HttpServletResponse response) {
		final Cliente clienteSalvo = clienteService.criar(cliente);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, clienteSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cliente> atualizar(@PathVariable("id") Integer id, @Valid @RequestBody Cliente cliente) {
		final Cliente clienteSalvo = clienteService.atualizar(id, cliente);
		return ResponseEntity.status(HttpStatus.OK).body(clienteSalvo);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable("id") Integer id) {
		clienteService.excluir(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

}
