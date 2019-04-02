package com.locadora.infra.enums;

public enum StatusLocacao {
  ABERTO("aberto"), FINALIZADO("finalizado");
  
  private final String status;
  
  StatusLocacao(String status) {
    this.status=status;
  }
  
  public String getStatus() {
    return this.status;
  }
  
}
