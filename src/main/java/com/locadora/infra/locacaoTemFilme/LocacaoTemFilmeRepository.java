package com.locadora.infra.locacaoTemFilme;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.locadora.infra.filme.Filme;

public interface LocacaoTemFilmeRepository extends JpaRepository<LocacaoTemFilme, Integer> {

  List<LocacaoTemFilme> findByFilme(Filme filme);
  
}
