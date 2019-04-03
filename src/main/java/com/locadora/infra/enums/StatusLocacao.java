package com.locadora.infra.enums;

public enum StatusLocacao {
  ABERTO("Aberto"), FINALIZADO("Finalizado");
  
  private final String status;
  
  StatusLocacao(String status) {
    this.status=status;
  }
  
  public String getStatus() {
    return this.status;
  }
  
}