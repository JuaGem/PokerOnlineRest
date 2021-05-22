package com.projectpokerrest.pokerrest.web.api;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projectpokerrest.pokerrest.model.InsertUtenteParam;
import com.projectpokerrest.pokerrest.model.InsertUtenteParamFromAdmin;
import com.projectpokerrest.pokerrest.model.Ruolo;
import com.projectpokerrest.pokerrest.model.StatoUtente;
import com.projectpokerrest.pokerrest.model.Utente;
import com.projectpokerrest.pokerrest.service.ruolo.RuoloService;
import com.projectpokerrest.pokerrest.service.utente.UtenteService;
import com.projectpokerrest.pokerrest.web.api.exception.UtenteNotFoundException;

@RestController
@RequestMapping("api/gestioneamministazione")
public class GestioneAmministazioneController {

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private RuoloService ruoloService;
	

	@GetMapping
	public List<Utente> listAll(@RequestHeader("Authorization") String user) {
		Utente utenteInSession = utenteService.findByUsername(user);
		
		if(utenteInSession == null)
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");

		if (!utenteInSession.getRuoli()
				.contains(ruoloService.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN")))
			throw new UtenteNotFoundException("Operazione non consentita, non sei admin");

		return utenteService.listAllUtenti();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Utente createUtente(@Validated(InsertUtenteParamFromAdmin.class) @RequestBody Utente utente,
			@RequestHeader("Authorization") String user) {
		Utente utenteInSession = utenteService.findByUsername(user);
		
		if(utenteInSession == null)
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");

		if (!utenteInSession.getRuoli()
				.contains(ruoloService.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN")))
			throw new UtenteNotFoundException("Operazione non consentita, non sei admin");

		utente.setDateCreated(new Date());
		utente.setStato(StatoUtente.CREATO);

		if (utente.getRuoli() != null) {
			for (Ruolo ruoloInstance : utente.getRuoli())
				utenteService.aggiungiRuolo(utente, ruoloInstance);
		}

		return utenteService.inserisciNuovo(utente);
	}

	@PostMapping("/registration")
	@ResponseStatus(HttpStatus.CREATED)
	public Utente registrationUtente(@Validated(InsertUtenteParam.class) @RequestBody Utente utente) {

		utente.setDateCreated(new Date());
		utente.setStato(StatoUtente.CREATO);
		utente.setEsperienzaAccumulata(0.0);
		utente.setCreditoResiduo(0.0);
		utente.setCreditoAccumulato(0.0);
		Set<Ruolo> ruoli = new HashSet<Ruolo>();
		ruoli.add(ruoloService.cercaPerDescrizioneECodice("Player User", "ROLE_PLAYER"));
		utente.setRuoli(ruoli);

		return utenteService.inserisciNuovo(utente);
	}

	@PutMapping("/{id}")
	public Utente update(@Validated(InsertUtenteParamFromAdmin.class) @RequestBody Utente utenteInput,
			@PathVariable(required = true) Long id, @RequestHeader("Authorization") String user) {
		Utente utenteInSession = utenteService.findByUsername(user);
		
		if(utenteInSession == null)
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");

		if (!utenteInSession.getRuoli()
				.contains(ruoloService.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN")))
			throw new UtenteNotFoundException("Operazione non consentita, non sei admin");

		if (utenteInput == null)
			throw new UtenteNotFoundException("Utente not found con id: " + id);

		Utente utentePreModifica = utenteService.caricaSingoloUtente(id);
		utenteInput.setId(id);
		utenteInput.setDateCreated(utentePreModifica.getDateCreated());
		utenteInput.setStato(utentePreModifica.getStato());
		
		if(utenteInput.getRuoli() == null)
			utenteInput.setRuoli(utentePreModifica.getRuoli());

		return utenteService.aggiorna(utenteInput);
	}
	
	@PutMapping("/updatefree/{id}")
	public Utente updateFree(@Validated(InsertUtenteParam.class) @RequestBody Utente utenteInput,
			@PathVariable(required = true) Long id, @RequestHeader("Authorization") String user) {
		Utente utenteInSession = utenteService.findByUsername(user);
		
		if(utenteInSession == null)
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");
		
		if(!utenteInSession.getId().equals(id))
			throw new RuntimeException("Non puoi modificare le credenziali di un altro utente");
		
		if (utenteInput == null)
			throw new UtenteNotFoundException("Utente not found con id: " + id);
		
		Utente utentePreModifica = utenteService.caricaSingoloUtente(id);
		utenteInput.setId(id);
		utenteInput.setDateCreated(utentePreModifica.getDateCreated());
		utenteInput.setStato(utentePreModifica.getStato());
		utenteInput.setCreditoAccumulato(utentePreModifica.getCreditoAccumulato());
		utenteInput.setCreditoResiduo(utentePreModifica.getCreditoResiduo());
		utenteInput.setEsperienzaAccumulata(utentePreModifica.getEsperienzaAccumulata());
		utenteInput.setRuoli(utentePreModifica.getRuoli());
		
		return utenteService.aggiorna(utenteInput);
	}

	@GetMapping("/{id}")
	public Utente findById(@PathVariable(value = "id", required = true) long id,
			@RequestHeader("Authorization") String user) {
		Utente utenteInSession = utenteService.findByUsername(user);
		
		if(utenteInSession == null)
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");

		if (!utenteInSession.getRuoli()
				.contains(ruoloService.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN")))
			throw new UtenteNotFoundException("Operazione non consentita, non sei admin");

		Utente utente = utenteService.caricaSingoloUtente(id);

		if (utente == null)
			throw new UtenteNotFoundException("Utente not found con id: " + id);

		return utente;
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable(required = true) Long id, @RequestHeader("Authorization") String user) {
		Utente utenteInSession = utenteService.findByUsername(user);
		
		if(utenteInSession == null)
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");

		if (!utenteInSession.getRuoli()
				.contains(ruoloService.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN")))
			throw new UtenteNotFoundException("Operazione non consentita, non sei admin");

		Utente utente = utenteService.caricaSingoloUtente(id);

		if (utente == null)
			throw new UtenteNotFoundException("Utente not found con id: " + id);

		utenteService.rimuovi(utente);
	}

	@PostMapping("/search")
	public List<Utente> search(@RequestBody Utente example, @RequestHeader("Authorization") String user) {
		Utente utenteInSession = utenteService.findByUsername(user);
		
		if(utenteInSession == null)
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");

		if (!utenteInSession.getRuoli()
				.contains(ruoloService.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN")))
			throw new UtenteNotFoundException("Operazione non consentita, non sei admin");

		return utenteService.findByExample(example);
	}

}
