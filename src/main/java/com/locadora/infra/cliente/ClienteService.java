package com.locadora.infra.cliente;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.locadora.infra.cliente.exceptions.ClienteNaoEncontradoException;
import com.locadora.infra.cliente.exceptions.CpfJaCadastradoException;

@Service
public class ClienteService {

	@Autowired
	ClienteRepository clienteRepository;

	public List<Cliente> listarTodos() {
		return clienteRepository.findAll();
	}

	public Cliente buscarPorId(Integer id) {
		final Optional<Cliente> clienteOpt = clienteRepository.findById(id);
		if (!clienteOpt.isPresent()) {
			throw new ClienteNaoEncontradoException();
		}
		return clienteOpt.get();
	}

	public Cliente criar(Cliente cliente) {
		final Optional<Cliente> clienteMesmoCpf = buscarPorCpf(cliente.getCpf());
		if (clienteMesmoCpf.isPresent()) {
			throw new CpfJaCadastradoException();
		}
		cliente.setCpf(formatarCpf(cliente.getCpf()));
		return clienteRepository.save(cliente);
	}

	public Cliente atualizar(Integer id, Cliente cliente) {
		final Cliente clienteSalvo = buscarPorId(id);
		final Optional<Cliente> clienteMesmoCpf = buscarPorCpf(cliente.getCpf());
		if (clienteMesmoCpf.isPresent() && clienteMesmoCpf.get().getId() != id) {
			throw new CpfJaCadastradoException();
		}
		cliente.setCpf(formatarCpf(cliente.getCpf()));
		BeanUtils.copyProperties(cliente, clienteSalvo, "id");
		return clienteRepository.save(clienteSalvo);
	}

	public void excluir(Integer id) {
		final Cliente cliente = buscarPorId(id);
		clienteRepository.deleteById(cliente.getId());
	}

	private String formatarCpf(String cpf) {
		if (cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}")) {
			return cpf;
		} else {
			String cpfFormatado = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-"
					+ cpf.substring(9, 11);
			return cpfFormatado;
		}
	}

	public Optional<Cliente> buscarPorCpf(String cpf) {
		cpf = formatarCpf(cpf);
		return clienteRepository.findByCpf(cpf);
	}

	public Cliente buscarClientePorCpf(String cpf) {
		final Optional<Cliente> clienteOpt = buscarPorCpf(cpf);
		if (!clienteOpt.isPresent()) {
			throw new ClienteNaoEncontradoException();
		}
		return clienteOpt.get();
	}

}