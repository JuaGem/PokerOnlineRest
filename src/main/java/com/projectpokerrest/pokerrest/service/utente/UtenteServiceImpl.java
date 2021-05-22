package com.projectpokerrest.pokerrest.service.utente;

import com.projectpokerrest.pokerrest.model.Ruolo;
import com.projectpokerrest.pokerrest.model.StatoUtente;
import com.projectpokerrest.pokerrest.model.Utente;
import com.projectpokerrest.pokerrest.repository.utente.UtenteRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UtenteServiceImpl implements UtenteService {

	@Autowired
	private UtenteRepository repository;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional(readOnly = true)
	public List<Utente> listAllUtenti() {
		return (List<Utente>) repository.findAll();
	}

	@Transactional(readOnly = true)
	public Utente caricaSingoloUtente(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Transactional
	public Utente aggiorna(Utente utenteInstance) {
		return repository.save(utenteInstance);
	}

	@Transactional
	public Utente inserisciNuovo(Utente utenteInstance) {
		return repository.save(utenteInstance);
	}

	@Transactional
	public void rimuovi(Utente utenteInstance) {
		this.invertUserAbilitation(utenteInstance.getId());
	}

	@Transactional(readOnly = true)
	public List<Utente> findByExample(Utente example) {
		return repository.findByExample(example);
	}

	@Transactional(readOnly = true)
	public Utente findByUsernameAndPassword(String username, String password) {
		return repository.findByUsernameAndPassword(username, password);
	}

	@Transactional(readOnly = true)
	public Utente eseguiAccesso(String username, String password) {
		return repository.findByUsernameAndPasswordAndStato(username, password, StatoUtente.ATTIVO);
	}

	@Transactional
	public void invertUserAbilitation(Long utenteInstanceId) {
		Utente utenteInstance = caricaSingoloUtente(utenteInstanceId);
		if(utenteInstance == null)
			throw new RuntimeException("Elemento non trovato.");
		
		if(utenteInstance.getStato().equals(StatoUtente.ATTIVO))
			utenteInstance.setStato(StatoUtente.DISABILITATO);
		else if(utenteInstance.getStato().equals(StatoUtente.DISABILITATO) || utenteInstance.getStato().equals(StatoUtente.CREATO))
			utenteInstance.setStato(StatoUtente.ATTIVO);
	}

	@Transactional(readOnly = true)
	public Utente findByUsername(String username) {
		return repository.findByUsername(username).orElse(null);
	}

	@Transactional
	public void sottraiCredito(Utente utente, Double costoAnnuncio) {
		Double creditoResiduo = utente.getCreditoResiduo();
		creditoResiduo = creditoResiduo - costoAnnuncio;
		utente.setCreditoResiduo(creditoResiduo);
		repository.save(utente);
	}

	@Transactional(readOnly = true)
	public Utente caricaUtenteConRuoli(Long id) {
		return repository.findOneEagerRuoli(id).orElse(null);
	}
	
	@Transactional
	public void aggiungiRuolo(Utente utenteEsistente, Ruolo ruoloInstance) {
//		utenteEsistente = entityManager.merge(utenteEsistente);
//		ruoloInstance = entityManager.merge(ruoloInstance);

		utenteEsistente.getRuoli().add(ruoloInstance);
	}
	
	@Transactional(readOnly = true)
	public boolean unicoAdminAttivo() {
		return repository.countByUtenteAdminAttivo() == 1;
	}

}
