package com.locadora.infra.cliente;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Embeddable
public class Endereco {

  @NotBlank
  @Size(min = 5, max = 150)
  @Column(name = "RUA")
  private String rua;

  @NotBlank
  @Column(name = "CEP", length = 10)
  private String cep;

  @NotBlank
  @Size(min = 3, max = 50)
  @Column(name = "BAIRRO")
  private String bairro;

  @Size(max = 150)
  @Column(name = "COMPLEMENTO")
  private String complemento;

  @NotBlank
  @Size(min = 3, max = 50)
  @Column(name = "CIDADE")
  private String cidade;

  public Endereco() {
    super();
  }

  public Endereco(String rua, String cep, String bairro, String complemento, String cidade) {
    super();
    this.rua = rua;
    this.cep = cep;
    this.bairro = bairro;
    this.complemento = complemento;
    this.cidade = cidade;
  }

  public String getRua() {
    return rua;
  }

  public void setRua(String rua) {
    this.rua = rua;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getComplemento() {
    return complemento;
  }

  public void setComplemento(String complemento) {
    this.complemento = complemento;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  @JsonIgnore
  public String getJson() {
    
    String json =
        "{"
        + "\"rua\":"+this.addAspas(this.getRua())
        + ",\"cep\":"+this.addAspas(this.getCep())
        + ",\"bairro\":"+this.addAspas(this.getBairro())
        + ",\"complemento\":"+this.addAspas(this.getComplemento())
        + ",\"cidade\":"+this.addAspas(this.getCidade())
        +"}";
    
    return json;
  }

  private String addAspas(String atributo) {
    return "\""+atributo+"\"";
  }
  
}
