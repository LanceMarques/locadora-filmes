package com.locadora.infra.locacao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.locadora.infra.cliente.Cliente;
import com.locadora.infra.enums.StatusLocacao;

public interface LocacaoRepository extends JpaRepository<Locacao, Integer>, LocacaoRepositoryQuery{

  List<Locacao> findByCliente(Cliente cliente);
  List<Locacao> findByStatus(StatusLocacao status);
  
}
