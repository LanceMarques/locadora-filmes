package com.locadora.infra.cliente;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Classe responsavel por modelar e mapear os atributos da entidade Cliente
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@Entity
@Table(name = "CLIENTE")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank
	@CPF
	@Column(name = "CPF")
	private String cpf;

	@NotBlank
	@Size(min = 5, max = 150)
	@Column(name = "NOME")
	private String nome;

	@NotNull
	@Embedded
	private Endereco endereco;

	public Cliente() {
		super();
	}

	public Cliente(Integer id, String cpf, String nome, Endereco endereco) {
		super();
		this.id = id;
		this.cpf = cpf;
		this.nome = nome;
		this.endereco = endereco;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@JsonIgnore
	public String getJson() {
	  
	  //new Cliente(id, cpf, nome, endereco)
	  
	  String json = "{"
	      + "\"id\":"+this.getId()
	      + ",\"cpf\":"+this.addAspas(this.getCpf())
	      + ",\"nome\":"+this.addAspas(this.getNome())
	      + ",\"endereco\":"+this.getEndereco().getJson()
	      + "}";
	  
	  return json;
	  
	}
	
	@JsonIgnore
	private String addAspas(String atributo) {
	  return "\""+atributo+"\"";
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
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
