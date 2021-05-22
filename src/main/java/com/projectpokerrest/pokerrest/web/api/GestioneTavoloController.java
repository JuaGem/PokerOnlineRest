package com.projectpokerrest.pokerrest.web.api;

import com.projectpokerrest.pokerrest.model.InsertTavoloParam;
import com.projectpokerrest.pokerrest.model.Tavolo;
import com.projectpokerrest.pokerrest.model.Utente;
import com.projectpokerrest.pokerrest.service.ruolo.RuoloService;
import com.projectpokerrest.pokerrest.service.tavolo.TavoloService;
import com.projectpokerrest.pokerrest.service.utente.UtenteService;
import com.projectpokerrest.pokerrest.web.api.exception.UtenteNotFoundException;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/gestionetavolo")
public class GestioneTavoloController {

	@Autowired
	private TavoloService tavoloService;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private RuoloService ruoloService;

	@PostMapping("/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Tavolo createTavolo(@Validated(InsertTavoloParam.class) @RequestBody Tavolo tavolo,
			@PathVariable(required = true) Long id, @RequestHeader("Authorization") String user) {
		Utente utenteInSession = utenteService.findByUsername(user);
		
		if(utenteInSession == null)
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");

		if (utenteInSession.getRuoli().contains(ruoloService.cercaPerDescrizioneECodice("Player User", "ROLE_PLAYER"))
				&& utenteInSession.getRuoli().size() == 1)
			throw new UtenteNotFoundException("Operazione non consentita, non sei ne admin, ne special");

		tavolo.setDataCreazione(new Date());

		if (utenteInSession.getRuoli()
				.contains(ruoloService.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN"))) {
			tavolo.setUtenteCreazione(utenteService.caricaSingoloUtente(id));
			return tavoloService.inserisciNuovo(tavolo);
		} 

		tavolo.setUtenteCreazione(utenteInSession);
		return tavoloService.inserisciNuovo(tavolo);
	}

	@GetMapping
	public List<Tavolo> listAll(@RequestHeader("Authorization") String user) {
		Utente utenteInSession = utenteService.findByUsername(user);

		if (!utenteInSession.getRuoli()
				.contains(ruoloService.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN")))
			throw new UtenteNotFoundException("Operazione non consentita, non sei admin");

		return tavoloService.listAllEager();
	}

	@PostMapping("/{idTavolo}/{idUtente}")
	@ResponseStatus(HttpStatus.OK)
	public void associaUtenteAlTavolo(@PathVariable(required = true) Long idTavolo,
			@PathVariable(required = true) Long idUtente, @RequestHeader("Authorization") String user) {
		Utente utenteInSession = utenteService.findByUsername(user);
		
		if(utenteInSession == null)
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");

		if (utenteInSession.getRuoli().contains(ruoloService.cercaPerDescrizioneECodice("Player User", "ROLE_PLAYER"))
				&& utenteInSession.getRuoli().size() == 1)
			throw new UtenteNotFoundException("Operazione non consentita, non sei ne admin, ne special");

		Utente utente = utenteService.caricaSingoloUtente(idUtente);
		Tavolo tavolo = tavoloService.caricaSingoloTavolo(idTavolo);

		if (utenteInSession.getRuoli()
				.contains(ruoloService.cercaPerDescrizioneECodice("Special Player User", "ROLE_SPECIAL_PLAYER"))
				&& !utenteInSession.getRuoli()
						.contains(ruoloService.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN"))
				&& !tavolo.getUtenteCreazione().equals(utenteInSession))
			throw new UtenteNotFoundException("Operazione non consentita");

		utente.setTavolo(tavolo);
		utenteService.aggiorna(utente);
	}

}
