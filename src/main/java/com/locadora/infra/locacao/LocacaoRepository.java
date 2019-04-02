package com.locadora.infra.locacao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.locadora.infra.cliente.Cliente;

public interface LocacaoRepository extends JpaRepository<Locacao, Integer> {

  List<Locacao> findByCliente(Cliente cliente);
  
}
