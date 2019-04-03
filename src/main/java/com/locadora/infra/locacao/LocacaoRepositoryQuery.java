package com.locadora.infra.locacao;

import java.util.List;

public interface LocacaoRepositoryQuery {
  public List<Locacao>filtrar(LocacaoFilter filter);
}
