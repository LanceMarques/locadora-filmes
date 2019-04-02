package com.locadora.infra.locacao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import com.locadora.infra.cliente.Cliente;
import com.locadora.infra.enums.StatusLocacao;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilme;

@Entity
@Table(name = "LOCACAO")
public class Locacao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @DateTimeFormat(pattern = "dd/MM/yyyy")
  @Column(name = "DATA_REALIZACAO")
  private Date dataRealizacao;

  @DateTimeFormat(pattern = "dd/MM/yyyy")
  @Column(name = "DATA_DEVOLUCAO")
  private Date dataDevolucao;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private StatusLocacao status;

  @Column(name = "VALOR_TOTAL")
  private Double valorTotal;

  @NotNull
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "CLIENTE_ID")
  private Cliente cliente;

  @OneToMany(mappedBy = "locacao", cascade = CascadeType.ALL)
  private List<LocacaoTemFilme> filmes = new ArrayList<LocacaoTemFilme>();

  public Locacao() {
    super();
  }

  public Locacao(Integer id, Date dataRealizacao, Date dataDevolucao, StatusLocacao status,
      Double valorTotal, @NotNull Cliente cliente, List<LocacaoTemFilme> filmes) {
    super();
    this.id = id;
    this.dataRealizacao = dataRealizacao;
    this.dataDevolucao = dataDevolucao;
    this.status = status;
    this.valorTotal = valorTotal;
    this.cliente = cliente;
    this.filmes = filmes;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Date getDataRealizacao() {
    return dataRealizacao;
  }

  public void setDataRealizacao(Date dataRealizacao) {
    this.dataRealizacao = dataRealizacao;
  }

  public Date getDataDevolucao() {
    return dataDevolucao;
  }

  public void setDataDevolucao(Date dataDevolucao) {
    this.dataDevolucao = dataDevolucao;
  }

  public StatusLocacao getStatus() {
    return status;
  }

  public void setStatus(StatusLocacao status) {
    this.status = status;
  }

  public Double getValorTotal() {
    return valorTotal;
  }

  public void setValorTotal(Double valorTotal) {
    this.valorTotal = valorTotal;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public List<LocacaoTemFilme> getFilmes() {
    return filmes;
  }

  public void setFilmes(List<LocacaoTemFilme> filmes) {
    this.filmes = filmes;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Locacao other = (Locacao) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}
