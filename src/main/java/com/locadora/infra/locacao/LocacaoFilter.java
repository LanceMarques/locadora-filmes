package com.locadora.infra.locacao;

import java.sql.Date;

public class LocacaoFilter {
 
  private Date dataRealizacaoDe;
  private Date dataRealizacaoAte;
  
  private Date dataDevolucaoDe;
  private Date dataDevolucaoAte;
  
  public Date getDataRealizacaoDe() {
    return dataRealizacaoDe;
  }
  public void setDataRealizacaoDe(Date dataRealizacaoDe) {
    this.dataRealizacaoDe = dataRealizacaoDe;
  }
  public Date getDataRealizacaoAte() {
    return dataRealizacaoAte;
  }
  public void setDataRealizacaoAte(Date dataRealizacaoAte) {
    this.dataRealizacaoAte = dataRealizacaoAte;
  }
  public Date getDataDevolucaoDe() {
    return dataDevolucaoDe;
  }
  public void setDataDevolucaoDe(Date dataDevolucaoDe) {
    this.dataDevolucaoDe = dataDevolucaoDe;
  }
  public Date getDataDevolucaoAte() {
    return dataDevolucaoAte;
  }
  public void setDataDevolucaoAte(Date dataDevolucaoAte) {
    this.dataDevolucaoAte = dataDevolucaoAte;
  }
  
  
  
}
