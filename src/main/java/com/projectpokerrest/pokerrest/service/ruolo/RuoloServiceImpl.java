package com.projectpokerrest.pokerrest.service.ruolo;

import com.projectpokerrest.pokerrest.model.Ruolo;
import com.projectpokerrest.pokerrest.repository.ruolo.RuoloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class RuoloServiceImpl implements RuoloService {

	@Autowired
	private RuoloRepository repository;
	
	@Transactional(readOnly = true)
	public List<Ruolo> listAll() {
		return (List<Ruolo>) repository.findAll();
	}

	@Transactional(readOnly = true)
	public Ruolo caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Transactional
	public void aggiorna(Ruolo ruoloInstance) {
		repository.save(ruoloInstance);
	}

	@Transactional
	public void inserisciNuovo(Ruolo ruoloInstance) {
		repository.save(ruoloInstance);
	}

	@Transactional
	public void rimuovi(Ruolo ruoloInstance) {
		repository.delete(ruoloInstance);
	}

	@Transactional(readOnly = true)
	public Ruolo cercaPerDescrizioneECodice(String descrizione, String codice) {
		return repository.findByDescrizioneAndCodice(descrizione, codice);
	}

}
