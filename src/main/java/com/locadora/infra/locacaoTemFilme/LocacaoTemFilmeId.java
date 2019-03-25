package com.locadora.infra.locacaoTemFilme;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LocacaoTemFilmeId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "LOCACAO_ID")
	private Integer locacaoId;
	
	@Column(name = "FILME_ID")
	private Integer filmeId;
	
	private LocacaoTemFilmeId() {}
	
	public LocacaoTemFilmeId(Integer locacaoId, Integer filmeId) {
		this.locacaoId = locacaoId;
		this.filmeId = filmeId;
	}

	public Integer getLocacaoId() {
		return locacaoId;
	}

	public void setLocacaoId(Integer locacaoId) {
		this.locacaoId = locacaoId;
	}

	public Integer getFilmeId() {
		return filmeId;
	}

	public void setFilmeId(Integer filmeId) {
		this.filmeId = filmeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filmeId == null) ? 0 : filmeId.hashCode());
		result = prime * result + ((locacaoId == null) ? 0 : locacaoId.hashCode());
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
		LocacaoTemFilmeId other = (LocacaoTemFilmeId) obj;
		if (filmeId == null) {
			if (other.filmeId != null)
				return false;
		} else if (!filmeId.equals(other.filmeId))
			return false;
		if (locacaoId == null) {
			if (other.locacaoId != null)
				return false;
		} else if (!locacaoId.equals(other.locacaoId))
			return false;
		return true;
	}
	
	
	
}
