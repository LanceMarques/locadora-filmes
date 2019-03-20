package com.locadora.infra.cliente;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.locadora.infra.cliente.exceptions.ClienteNaoEncontradoException;
import com.locadora.infra.cliente.exceptions.CpfJaCadastradoException;

@Service
public class ClienteService {
	
	@Autowired
	ClienteRepository clienteRepository;
	
	public List<Cliente> listarTodos(){
		return clienteRepository.findAll();
	}

	public Cliente buscarPorId(int id){
		Optional<Cliente> clienteOpt = clienteRepository.findById(id);
		if(!clienteOpt.isPresent()) {
			throw new ClienteNaoEncontradoException();
		}
		return clienteOpt.get();
	}
	
	public Cliente criar(Cliente cliente) {
		Optional<Cliente> clienteOpt = buscarPorCpf(cliente.getCpf());
		if(clienteOpt.isPresent()) {
			throw new CpfJaCadastradoException();
		}
		cliente.setCpf(formatarCpf(cliente.getCpf()));
		return clienteRepository.save(cliente);
	}

	public Cliente atualizar(int id, Cliente cliente) {
		Optional<Cliente> clienteOpt = buscarPorCpf(cliente.getCpf());
		if(clienteOpt.isPresent()&&clienteOpt.get().getId()!=id) {
			throw new CpfJaCadastradoException();
		}
		Cliente clienteSalvo = buscarPorId(id);
		BeanUtils.copyProperties(cliente, clienteSalvo, "id");
		return clienteSalvo;
	}
	
	public void excluir(int id) {
		Cliente cliente =buscarPorId(id);
		clienteRepository.deleteById(cliente.getId());
	}
	
	private String formatarCpf(String cpf) {
		if(cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}")) {
			return cpf;
		}else {
			String cpfFormatado = 	cpf.substring(0,3)+"."+
					cpf.substring(3,6)+"."+
					cpf.substring(6,9)+"-"+
					cpf.substring(9,11);
					return cpfFormatado;
		}
	}
	
	private Optional<Cliente> buscarPorCpf(String cpf) {
		cpf = formatarCpf(cpf);
		Optional<Cliente> clienteOpt = clienteRepository.findByCpf(cpf);
		return clienteOpt;
	}
	
}