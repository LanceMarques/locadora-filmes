package com.locadora.infra.genero;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneroRepository extends JpaRepository<Genero, Integer> {

  Optional<Genero> findByNome(String nome);

}
