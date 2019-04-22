package com.locadora.infra.genero;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Classe responsavel por modelar e mapear os atributos da entidade Genero
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@Entity
@Table(name = "GENERO")
public class Genero {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank
  @Size(min = 3, max = 50)
  @Column(name = "NOME", unique = true, length = 50)
  private String nome;

  public Genero() {
  }

  public Genero(Integer id, String nome) {
    this.setId(id);
    this.setNome(nome);
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
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
    Genero other = (Genero) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}
