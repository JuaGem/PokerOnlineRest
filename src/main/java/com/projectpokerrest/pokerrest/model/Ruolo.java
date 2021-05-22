package com.projectpokerrest.pokerrest.model;

import javax.persistence.*;

@Entity
@Table(name = "ruolo")
public class Ruolo {

	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_PLAYER = "ROLE_PLAYER";
	public static final String ROLE_SPECIAL_PLAYER = "ROLE_SPECIAL_PLAYER";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "descrizione")
	private String descrizione;
	@Column(name = "codice")
	private String codice;

	public Ruolo() {
	}

	public Ruolo(String descrizione, String codice) {
		this.descrizione = descrizione;
		this.codice = codice;
	}
	
	public Ruolo(String codice) {
		this.codice = codice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Ruolo))
			return false;
		
		Ruolo other = (Ruolo) obj;
		
		return this.codice.equals(other.codice);
	}
	
	

}
