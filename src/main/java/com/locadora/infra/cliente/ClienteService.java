package com.locadora.infra.cliente;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.locadora.infra.cliente.exceptions.ClienteNaoEncontradoException;
import com.locadora.infra.cliente.exceptions.CpfJaCadastradoException;

/**
 * Classe responsavel por implementar as funcionalidades e validacoes
 * solicitadas pelo controller da entidade Cliente
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	/**
	 * Metodo responsavel por fornecer uma lista com todos os {@link Cliente
	 * clientes} cadastrados no sistema
	 * 
	 * @return {@link List Lista} com todos os {@link Cliente clientes} cadastrados
	 */
	public List<Cliente> listarTodos() {
		return this.clienteRepository.findAll();
	}

	/**
	 * Metodo responsavel por fornecer um {@link Cliente cliente} que possui o id
	 * informado.
	 * 
	 * @param id {@link Integer} Id requisitado na pesquisa
	 * @return {@link Cliente Cliente} que possui o id informado.
	 * 
	 * @since 1.0.0
	 */
	public Cliente buscarPorId(Integer id) {
		final Optional<Cliente> clienteOpt = this.clienteRepository.findById(id);
		if (!clienteOpt.isPresent()) {
			throw new ClienteNaoEncontradoException();
		}
		return clienteOpt.get();
	}

	/**
	 * Metodo responsavel por fornecer um {@link Cliente cliente} que possui o cpf
	 * recebido como parametro.
	 * 
	 * @param cpf ({@link String}) CPF requisitado na pesquisa.
	 * @return {@link Optional Optional} contendo um cliente que possua o CPF consultado.
	 * 
	 * @since 1.0.0
	 */
	public Optional<Cliente> buscarPorCpf(String cpf) {
		final String cpfFormatado = formatarCpf(cpf);
		return this.clienteRepository.findByCpf(cpfFormatado);
	}

	/**
	 * Metodo responsavel por fornecer um {@link Cliente cliente} que possui o cpf
	 * informado. Lanca uma excecao caso o cliente nao for encontrado.
	 * 
	 * @param cpf {@link String} CPF requisitado na pesquisa.
	 * @return {@link Optional Optional} contendo um cliente que possua o CPF consultado.
	 * 
	 * @since 1.0.0
	 */
	public Cliente buscarClientePorCpf(String cpf) {
		final Optional<Cliente> clienteOpt = buscarPorCpf(cpf);

		if (!clienteOpt.isPresent()) {
			throw new ClienteNaoEncontradoException();
		}
		return clienteOpt.get();
	}

	/**
	 * Metodo responsavel por cadastrar um cliente recebido como parametro
	 * 
	 * @param cliente {@link Cliente} Cliente recebido como parametro para cadastro
	 *        no sistema
	 * @return {@link Cliente Cliente} cadastrado no sistema.
	 * 
	 * @since 1.0.0
	 */
	public Cliente criar(Cliente cliente) {
		final Optional<Cliente> clienteMesmoCpf = buscarPorCpf(cliente.getCpf());
		if (clienteMesmoCpf.isPresent()) {
			throw new CpfJaCadastradoException();
		}
		cliente.setCpf(formatarCpf(cliente.getCpf()));
		return this.clienteRepository.save(cliente);
	}

	/**
	 * Metodo responsavel por atualizar um {@link Cliente cliente} que possui o id
	 * solicitado com os dados recebidos no corpo da requisicao.
	 * 
	 * @param id      ({@link Integer}) Id do cliente a ser atualizado.
	 * @param cliente {@link Cliente} Dados atualizados.
	 * 
	 * @since 1.0.0
	 */
	public Cliente atualizar(Integer id, Cliente cliente) {
		final Cliente clienteSalvo = buscarPorId(id);
		final Optional<Cliente> clienteMesmoCpf = buscarPorCpf(cliente.getCpf());

		if (clienteMesmoCpf.isPresent() && clienteMesmoCpf.get().getId() != id) {
			throw new CpfJaCadastradoException();
		}

		cliente.setCpf(formatarCpf(cliente.getCpf()));

		BeanUtils.copyProperties(cliente, clienteSalvo, "id");

		return this.clienteRepository.save(clienteSalvo);
	}

	/**
	 * Metodo responsavel por excluir um {@link Cliente cliente} que possui o id
	 * informado.
	 * 
	 * @param id ({@link Integer}) Id do cliente a ser excluido.
	 * 
	 * @since 1.0.0
	 */
	public void excluir(Integer id) {
		final Cliente cliente = buscarPorId(id);
		this.clienteRepository.deleteById(cliente.getId());
	}

	/**
	 * Metodo responsavel por formatar o cpf recebido como parametro
	 * 
	 * @param cpf ({@link String}) cpf a ser formatado
	 * @return cpfFormatado {@link String} cpf formatado com pontos e traço
	 * 
	 * @since 1.0.0
	 */
	private String formatarCpf(String cpf) {
		final String cpfFormatado;
		if (cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}")) {
			cpfFormatado = cpf;
		} else {
			cpfFormatado = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-"
					+ cpf.substring(9, 11);
		}
		return cpfFormatado;
	}

}