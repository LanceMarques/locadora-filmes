package com.locadora.infra.filme;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.locadora.infra.genero.Genero;

@Entity
@Table(name = "FILME")
public class Filme {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;

	@NotBlank
	@Column(name = "TITULO", length = 50)
	private String titulo;

	@NotNull
	@Column(name = "DURACAO")
	private int duracao;

	@NotNull
	@Column(name = "QUANTIDADE_ESTOQUE")
	private int quantidadeEstoque;

	@NotBlank
	@Column(name = "SINOPSE")
	private String sinopse;

	@NotBlank
	@Column(name = "NOME_DIRETOR", length = 150)
	private String nomeDiretor;

	@NotNull
	@Column(name = "VALOR_DIARIA", precision = 4, scale = 2)
	private double valorDiaria;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "GENERO_ID")
	private Genero genero;

	public Filme() {
		super();
	}

	public Filme(Integer id, String titulo, int duracao, int quantidadeEstoque, String sinopse, String nomeDiretor,
			double valorDiaria, Genero genero) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.duracao = duracao;
		this.quantidadeEstoque = quantidadeEstoque;
		this.sinopse = sinopse;
		this.nomeDiretor = nomeDiretor;
		this.valorDiaria = valorDiaria;
		this.genero = genero;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getDuracao() {
		return duracao;
	}

	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}

	public int getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(int quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public String getSinopse() {
		return sinopse;
	}

	public void setSinopse(String sinopse) {
		this.sinopse = sinopse;
	}

	public String getNomeDiretor() {
		return nomeDiretor;
	}

	public void setNomeDiretor(String nomeDiretor) {
		this.nomeDiretor = nomeDiretor;
	}

	public double getValorDiaria() {
		return valorDiaria;
	}

	public void setValorDiaria(double valorDiaria) {
		this.valorDiaria = valorDiaria;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
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
		Filme other = (Filme) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
