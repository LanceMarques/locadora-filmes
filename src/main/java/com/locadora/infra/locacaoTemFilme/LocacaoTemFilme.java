package com.locadora.infra.locacaoTemFilme;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.locadora.infra.filme.Filme;
import com.locadora.infra.locacao.Locacao;

@Entity(name = "LocacaoTemFilme")
@Table(name = "LOCACAO_TEM_FILME")
public class LocacaoTemFilme {

  @EmbeddedId
  private LocacaoTemFilmeId id;

  @ManyToOne(fetch = FetchType.EAGER)
  @MapsId("locacaoId")
  private Locacao locacao;

  @ManyToOne(fetch = FetchType.EAGER)
  @MapsId("filmeId")
  private Filme filme;

  @NotNull
  @Min(1)
  @Max(5)
  @Column(name = "QUANTIDADE_LOCADA")
  private Integer quantidadeLocada;

  @NotNull
  @Column(name = "VALOR_TOTAL_DIARIA")
  private double valorTotalDaDiaria;

  public LocacaoTemFilme() {}

  public LocacaoTemFilme(Locacao locacao, Filme filme) {
    setLocacao(locacao);
    setFilme(filme);
    setId(new LocacaoTemFilmeId(locacao.getId(), filme.getId()));
  }

  public LocacaoTemFilme(Locacao locacao, Filme filme, Integer quantidadeLocada) {
    setLocacao(locacao);
    setFilme(filme);
    setQuantidadeLocada(quantidadeLocada);
    setId(new LocacaoTemFilmeId(locacao.getId(), filme.getId()));
  }


  public LocacaoTemFilmeId getId() {
    return id;
  }

  public void setId(LocacaoTemFilmeId id) {
    this.id = id;
  }

  @JsonIgnore
  public Locacao getLocacao() {
    return locacao;
  }

  public void setLocacao(Locacao locacao) {
    this.locacao = locacao;
  }

  public Filme getFilme() {
    return filme;
  }

  public void setFilme(Filme filme) {
    this.filme = filme;
  }

  public Integer getQuantidadeLocada() {
    return quantidadeLocada;
  }

  public void setQuantidadeLocada(Integer quantidadeLocada) {
    this.quantidadeLocada = quantidadeLocada;
  }

  public double getValorTotalDaDiaria() {
    return valorTotalDaDiaria;
  }

  public void setValorTotalDaDiaria(double i) {
    this.valorTotalDaDiaria = i;
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
    LocacaoTemFilme other = (LocacaoTemFilme) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}
