package com.locadora.locacao;

import static org.mockito.Mockito.when;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.locadora.infra.cliente.Cliente;
import com.locadora.infra.enums.StatusLocacao;
import com.locadora.infra.filme.Filme;
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
  private CriteriaBuilder criteriaBuilder;
  
  @MockBean
  private EntityManager entityManager;
  
  @MockBean
  private Root<Locacao> root;
    
  @Before
  public void setUp() throws Exception {
        when(this.entityManager.getCriteriaBuilder());
        
        
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

    List<LocacaoTemFilme> filmes = Arrays.asList(
        new LocacaoTemFilme()
      );
    
    Locacao locacao1 = new Locacao(1, DataUtils.gerarDataAtual(), null, StatusLocacao.ABERTO, 10.0, new Cliente(),filmes);
    Locacao locacao2 = new Locacao(2, DataUtils.gerarDataAtual(), null, StatusLocacao.ABERTO, 10.0, new Cliente(),filmes);
    
    return Arrays.asList(
        locacao1,
        locacao2        
    );
  }

}
