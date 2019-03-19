package com.locadora.infra.cliente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

	Optional<Cliente> findByCpf(String cpf);
	Optional<Cliente> findByNome(String nome);
	
}