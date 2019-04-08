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

/**
 * Classe responsavel por mapear as requisições realizadas nas URIs da entidade
 * Cliente
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ApplicationEventPublisher publisher;

	/**
	 * Metodo responsavel por fornecer uma lista com todos os {@link Cliente
	 * clientes} cadastrados.
	 * 
	 * @return {@link ResponseEntity} com todos os {@link Cliente clientes}
	 *         cadastrados.
	 * 
	 * @since 1.0.0
	 */
	@GetMapping
	public ResponseEntity<List<Cliente>> listarTodos() {
		final List<Cliente> clientes = this.clienteService.listarTodos();
		return ResponseEntity.status(HttpStatus.OK).body(clientes);
	}

	/**
	 * Metodo responsavel por fornecer um {@link Cliente cliente} que possui o id
	 * informado.
	 * 
	 * @param id ({@link Integer}) Id requisitado na pesquisa
	 * @return {@link ResponseEntity} com o cliente que possui o id informado.
	 * 
	 * @since 1.0.0
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscarPorId(@PathVariable("id") Integer id) {
		final Cliente cliente = this.clienteService.buscarPorId(id);
		return ResponseEntity.status(HttpStatus.OK).body(cliente);

	}

	/**
	 * Metodo responsavel por fornecer um {@link Cliente cliente} que possui o CPF
	 * informado.
	 * 
	 * @param cpf {@link String} CPF requisitado na pesquisa
	 * @return {@link ResponseEntity} com o cliente que possui o CPF informado.
	 * 
	 * @since 1.0.0
	 */
	@GetMapping("cpf/{cpf}")
	public ResponseEntity<Cliente> buscarPorCpf(@PathVariable("cpf") String cpf) {
		final Cliente cliente = this.clienteService.buscarClientePorCpf(cpf);
		return ResponseEntity.status(HttpStatus.OK).body(cliente);
	}

	/**
	 * Metodo responsavel por cadastrar um cliente recebido como parametro
	 * 
	 * @param cliente ({@link Cliente}) Cliente recebido como parametro para
	 *                cadastro no sistema
	 * @return {@link ResponseEntity} com o cliente{@link Cliente} cadastrado no
	 *         sistema.
	 * 
	 * @since 1.0.0
	 */
	@PostMapping
	public ResponseEntity<Cliente> criar(@RequestBody @Valid Cliente cliente, HttpServletResponse response) {
		final Cliente clienteSalvo = this.clienteService.criar(cliente);
		this.publisher.publishEvent(new RecursoCriadoEvent(this, response, clienteSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
	}

	/**
	 * Metodo responsavel por atualizar um {@link Cliente cliente} que possui o id
	 * recebido no path com os dados recebidos no corpo da requisicao.
	 * 
	 * @param id      ({@link Integer}) Id do cliente a ser atualizado.
	 * @param cliente {@link Cliente} Dados a serem atualizados.
	 * 
	 * @return {@link ResponseEntity} sem conteudo.
	 * 
	 * @since 1.0.0
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable("id") Integer id, @Valid @RequestBody Cliente cliente) {
		this.clienteService.atualizar(id, cliente);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	/**
	 * Metodo responsavel por excluir um {@link Cliente cliente} que possui o id
	 * informado.
	 * 
	 * @param id ({@link Integer}) Id do cliente a ser excluido.
	 * @return {@link ResponseEntity} sem conteudo.
	 * 
	 * @since 1.0.0
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable("id") Integer id) {
		this.clienteService.excluir(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

}
