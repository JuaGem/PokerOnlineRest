package com.projectpokerrest.pokerrest.service.ruolo;

import com.projectpokerrest.pokerrest.model.Ruolo;

import java.util.List;

public interface RuoloService {
	
	public List<Ruolo> listAll() ;

	public Ruolo caricaSingoloElemento(Long id) ;

	public void aggiorna(Ruolo ruoloInstance) ;

	public void inserisciNuovo(Ruolo ruoloInstance) ;

	public void rimuovi(Ruolo ruoloInstance) ;

	public Ruolo cercaPerDescrizioneECodice(String descrizione, String codice) ;

}
