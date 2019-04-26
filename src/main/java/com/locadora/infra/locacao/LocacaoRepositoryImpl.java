package com.locadora.infra.locacao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class LocacaoRepositoryImpl implements LocacaoRepositoryQuery {

  @PersistenceContext
  private EntityManager manager;

  @Override
  public List<Locacao> filtrar(LocacaoFilter locacaoFilter) {
    
    CriteriaBuilder builder = manager.getCriteriaBuilder();
    CriteriaQuery<Locacao> criteria = builder.createQuery(Locacao.class);
   
    Root<Locacao> root = criteria.from(Locacao.class);
    
    Predicate[] predicates = criarRestricoes(locacaoFilter, builder, root);
    criteria.where(predicates);

    TypedQuery<Locacao> query = manager.createQuery(criteria);
    return query.getResultList();
  }

  private Predicate[] criarRestricoes(LocacaoFilter locacaoFilter, CriteriaBuilder builder,
      Root<Locacao> root) {
    
    List<Predicate> predicates = new ArrayList<>();
    
    if(locacaoFilter.getDataRealizacaoDe()!=null) {
      predicates.add(
          builder.greaterThanOrEqualTo(root.get("dataRealizacao"), locacaoFilter.getDataRealizacaoDe())
      );
    }
    if(locacaoFilter.getDataRealizacaoAte()!=null) {
      predicates.add(
        builder.lessThanOrEqualTo(root.get("dataRealizacao"), locacaoFilter.getDataRealizacaoAte())
      );
    }
    if(locacaoFilter.getDataDevolucaoDe()!=null) {
      predicates.add(
        builder.greaterThanOrEqualTo(root.get("dataDevolucao"), locacaoFilter.getDataDevolucaoDe())
      );
    }
    if(locacaoFilter.getDataDevolucaoAte()!=null) {
      predicates.add(
        builder.lessThanOrEqualTo(root.get("dataDevolucao"), locacaoFilter.getDataDevolucaoAte())
      );
    }
    if(locacaoFilter.getStatusLocacao()!=null) {
      predicates.add(
          builder.equal(root.get("status"), locacaoFilter.getStatusLocacao())
      );
    }
    return predicates.toArray(new Predicate[predicates.size()]);
  }

}
