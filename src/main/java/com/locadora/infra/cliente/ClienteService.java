package com.locadora.infra.cliente;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.locadora.infra.cliente.exceptions.ClienteNaoEncontradoException;
import com.locadora.infra.cliente.exceptions.CpfJaCadastradoException;
import com.locadora.infra.cliente.exceptions.MesmoNomeCadastradoException;

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
		Cliente clienteSalvo = cliente;
		clienteSalvo.setCpf(formatarCpf(clienteSalvo.getCpf()));
		
		Optional<Cliente> clienteOpt = clienteRepository.findByCpf(clienteSalvo.getCpf());
		if(clienteOpt.isPresent()){
			throw new CpfJaCadastradoException();
		}
		return clienteRepository.save(clienteSalvo);
	}

	public Cliente atualizar(int id, Cliente cliente) {
		Cliente clienteSalvo = buscarPorId(id);
		BeanUtils.copyProperties(cliente, clienteSalvo, "id");
		return clienteSalvo;
	}
	
	public void excluir(int id) {
		clienteRepository.deleteById(id);
	}
	
	private String formatarCpf(String cpf) {
		//remove simbolos não númericos do CPF
		String numerosCpf = cpf.replaceAll("\\D", "");
		//Adiciona simbolos corretamente ao CPF
		String cpfFormatado = numerosCpf.substring(0,3)+"."+
								numerosCpf.substring(3,6)+"."+
								numerosCpf.substring(6,9)+"-"+
								numerosCpf.substring(9,11);
		return cpfFormatado;
	}
		
}