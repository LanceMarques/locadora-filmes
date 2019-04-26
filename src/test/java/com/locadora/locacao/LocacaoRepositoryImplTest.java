package com.locadora.locacao;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.locadora.infra.cliente.Cliente;
import com.locadora.infra.enums.StatusLocacao;
import com.locadora.infra.locacao.Locacao;
import com.locadora.infra.locacao.LocacaoFilter;
import com.locadora.infra.locacao.LocacaoRepositoryImpl;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilme;
import com.locadora.utils.DataUtils;

@RunWith(MockitoJUnitRunner.Silent.class)
public class LocacaoRepositoryImplTest {

  @InjectMocks
  private LocacaoRepositoryImpl locacaoRepositoryImpl;

  @Mock
  private EntityManager entityManager;

  @Mock
  private CriteriaBuilder criteriaBuilder;

  @Mock
  private CriteriaQuery<Locacao> criteriaQuery;

  @Mock
  private Root<Locacao> root;

  @Mock
  private Path path;

  @Mock
  private List<Predicate> predicates;

  @Mock
  private Predicate predicate;

  @Mock
  private TypedQuery<Locacao> typedQuery;

  @Before
  public void setUp() throws Exception {
    
    when(entityManager.getCriteriaBuilder())
      .thenReturn(this.criteriaBuilder);

    when(criteriaBuilder.createQuery(Locacao.class)).thenReturn(this.criteriaQuery);

    when(criteriaQuery.from(Locacao.class))
      .thenReturn(this.root);

    when(entityManager.createQuery(criteriaQuery))
      .thenReturn(this.typedQuery);

    when(root.get("dataRealizacao"))
      .thenReturn(this.path);
    when(root.get("dataDevolucao"))
      .thenReturn(this.path);
    when(root.get("status"))
      .thenReturn(this.path);
    
    when(criteriaBuilder.equal(this.path, StatusLocacao.ABERTO)).thenReturn(this.predicate);

    when(typedQuery.getResultList()).thenReturn(this.criarListaLocacoes());
    
    

  }

  @Test
  public void testFiltrar() {

    LocacaoFilter locacaoFilter = this.criarLocacaoFilter();

    this.locacaoRepositoryImpl.filtrar(locacaoFilter);
  }

  private LocacaoFilter criarLocacaoFilter() {
    LocacaoFilter locacaoFilter = new LocacaoFilter(null, null, null, null, StatusLocacao.ABERTO);
    return locacaoFilter;
  }

  private List<Locacao> criarListaLocacoes() {

    List<LocacaoTemFilme> filmes = Arrays.asList(new LocacaoTemFilme());

    Locacao locacao1 = new Locacao(1, DataUtils.gerarDataAtual(), null, StatusLocacao.ABERTO, 10.0,
        new Cliente(), filmes);
    Locacao locacao2 = new Locacao(2, DataUtils.gerarDataAtual(), null, StatusLocacao.ABERTO, 10.0,
        new Cliente(), filmes);

    return Arrays.asList(locacao1, locacao2);
  }

}
