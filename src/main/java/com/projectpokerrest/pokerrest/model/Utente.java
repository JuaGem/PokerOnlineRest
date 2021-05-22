package com.projectpokerrest.pokerrest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "utente")
public class Utente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank(message = "{username.notblank}",  groups = {InsertUtenteParam.class, InsertUtenteParamFromAdmin.class})
	@Column(name = "username", unique = true)
	private String username;

	@NotBlank(message = "{password.notblank}",  groups = {InsertUtenteParam.class, InsertUtenteParamFromAdmin.class})
	@Column(name = "password")
	private String password;

	@NotBlank(message = "{nome.notblank}",  groups = {InsertUtenteParam.class, InsertUtenteParamFromAdmin.class})
	@Column(name = "nome")
	private String nome;

	@NotBlank(message = "{cognome.notblank}",  groups = {InsertUtenteParam.class, InsertUtenteParamFromAdmin.class})
	@Column(name = "cognome")
	private String cognome;

	@NotNull(message = "{dateCreated.notnull}")
	@Column(name = "dateCreated")
	private Date dateCreated;

	@NotNull(message = "{stato.notnull}")
	@Enumerated(EnumType.STRING)
	private StatoUtente stato;

	@NotNull(message = "{esperienzaAccumulata.notnull}", groups = {InsertUtenteParamFromAdmin.class})
	@DecimalMin("0.0")
	@Column(name = "esperienza_accumulata")
	private Double esperienzaAccumulata;

	@NotNull(message = "{creditoAccumulato.notnull}", groups = {InsertUtenteParamFromAdmin.class})
	@DecimalMin("0.0")
	@Column(name = "credito_accumulato")
	private Double creditoAccumulato;

	@NotNull(message = "{creditoResiduo.notnull}", groups = {InsertUtenteParamFromAdmin.class})
	@DecimalMin("0.0")
	@Column(name = "credito_residuo")
	private Double creditoResiduo;

	@ManyToMany
	@JoinTable(name = "utente_ruolo", joinColumns = @JoinColumn(name = "utente_id", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "ruolo_id", referencedColumnName = "ID"))
	private Set<Ruolo> ruoli = new HashSet<>(0);

//	@NotNull(message = "{tavolo.notnull}")
	@JsonIgnoreProperties(value = {"utenti", "utenteCreazione"})
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tavolo_id")
	private Tavolo tavolo;

	public Utente() {
	}

	public Utente(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public Utente(String username, String password, String nome, String cognome, Date dateCreated) {
		this.username = username;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.dateCreated = dateCreated;
	}
	
	public Utente(String username, String password, String nome, String cognome, Date dateCreated,StatoUtente stato, Double credito) {
		this.username = username;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.dateCreated = dateCreated;
		this.stato=stato;
		this.creditoResiduo=credito;
	}
	
	public Utente(String username, String password, String nome, String cognome, Date dateCreated,StatoUtente stato) {
		this.username = username;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.dateCreated = dateCreated;
		this.stato=stato;
	}

	public Utente(@NotBlank(message = "{nome.notblank}") String nome, @NotBlank(message = "{cognome.notblank}") String cognome, @NotNull(message = "{dateCreated.notnull}") Date dateCreated, @NotNull(message = "{stato.notnull}") StatoUtente stato, Double esperienzaAccumulata, Double creditoAccumulato, @Min(value = 0) Double creditoResiduo, Tavolo tavolo) {
		this.nome = nome;
		this.cognome = cognome;
		this.dateCreated = dateCreated;
		this.stato = stato;
		this.esperienzaAccumulata = esperienzaAccumulata;
		this.creditoAccumulato = creditoAccumulato;
		this.creditoResiduo = creditoResiduo;
		this.tavolo = tavolo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Ruolo> getRuoli() {
		return ruoli;
	}

	public void setRuoli(Set<Ruolo> ruoli) {
		this.ruoli = ruoli;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public StatoUtente getStato() {
		return stato;
	}

	public void setStato(StatoUtente stato) {
		this.stato = stato;
	}

	public Double getCreditoResiduo() {
		return creditoResiduo;
	}

	public void setCreditoResiduo(Double creditoResiduo) {
		this.creditoResiduo = creditoResiduo;
	}

	public boolean isAdmin() {
		for (Ruolo ruoloItem : ruoli) {
			if (ruoloItem.getCodice().equals(Ruolo.ROLE_ADMIN))
				return true;
		}
		return false;
	}

	public boolean isAttivo() {
		return this.stato.equals(StatoUtente.ATTIVO);
	}

	public Double getEsperienzaAccumulata() {
		return esperienzaAccumulata;
	}

	public void setEsperienzaAccumulata(Double esperienzaAccumulata) {
		this.esperienzaAccumulata = esperienzaAccumulata;
	}

	public Double getCreditoAccumulato() {
		return creditoAccumulato;
	}

	public void setCreditoAccumulato(Double creditoAccumulato) {
		this.creditoAccumulato = creditoAccumulato;
	}

	public Tavolo getTavolo() {
		return tavolo;
	}

	public void setTavolo(Tavolo tavolo) {
		this.tavolo = tavolo;
	}

	@Override
	public String toString() {
		return "Utente [id=" + id + ", username=" + username + ", stato=" + stato + ", ruoli=" + ruoli + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Utente))
			return false;
	
		Utente other = (Utente) obj;
		
		return this.id.equals(other.id);
	}
	
	

}
