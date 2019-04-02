package com.locadora.infra.filme;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.locadora.infra.genero.Genero;

public interface FilmeRepository extends JpaRepository<Filme, Integer> {

  List<Filme> findByGenero(Genero genero);

}
