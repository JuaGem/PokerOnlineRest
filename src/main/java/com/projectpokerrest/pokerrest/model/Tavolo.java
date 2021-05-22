package com.projectpokerrest.pokerrest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "tavolo")
public class Tavolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "{esperienzaMin.notnull}", groups = {InsertTavoloParam.class})
    @DecimalMin("0.0")
    @Column(name = "esperienza_min")
    private Double esperienzaMin;

    @NotNull(message = "{cifraMinima.notnull}", groups = {InsertTavoloParam.class})
    @DecimalMin("0.0")
    @Column(name = "cifra_minima")
    private Double cifraMinima;

    @NotNull(message = "{denominazione.notblank}", groups = {InsertTavoloParam.class})
    @Column(name = "denominazione")
    private String denominazione;

    @NotNull(message = "{dataCreazione.notnull}")
    @Column(name = "data_creazione")
    private Date dataCreazione;

    @JsonIgnoreProperties(value= {"tavolo"})
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tavolo")
    private Set<Utente> utenti = new HashSet<>();

    @NotNull(message = "{utenteCreazione.notnull}")
    @JsonIgnoreProperties(value= {"tavolo"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id")
    private Utente utenteCreazione;

    public Tavolo() {
    }

    public Tavolo(Double esperienzaMin, Double cifraMinima, String denominazione, Date dataCreazione) {
        this.esperienzaMin = esperienzaMin;
        this.cifraMinima = cifraMinima;
        this.denominazione = denominazione;
        this.dataCreazione = dataCreazione;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getEsperienzaMin() {
        return esperienzaMin;
    }

    public void setEsperienzaMin(Double esperienzaMin) {
        this.esperienzaMin = esperienzaMin;
    }

    public Double getCifraMinima() {
        return cifraMinima;
    }

    public void setCifraMinima(Double cifraMinima) {
        this.cifraMinima = cifraMinima;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Set<Utente> getUtenti() {
        return utenti;
    }

    public void setUtenti(Set<Utente> utenti) {
        this.utenti = utenti;
    }

    public Utente getUtenteCreazione() {
        return utenteCreazione;
    }

    public void setUtenteCreazione(Utente utenteCreazione) {
        this.utenteCreazione = utenteCreazione;
    }

}
