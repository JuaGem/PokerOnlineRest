package com.projectpokerrest.pokerrest.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projectpokerrest.pokerrest.model.Tavolo;
import com.projectpokerrest.pokerrest.model.Utente;
import com.projectpokerrest.pokerrest.service.ruolo.RuoloService;
import com.projectpokerrest.pokerrest.service.tavolo.TavoloService;
import com.projectpokerrest.pokerrest.service.utente.UtenteService;
import com.projectpokerrest.pokerrest.web.api.exception.GameRequirementsException;
import com.projectpokerrest.pokerrest.web.api.exception.TavoloNotFoundException;
import com.projectpokerrest.pokerrest.web.api.exception.UtenteNotFoundException;

@RestController
@RequestMapping("api/play")
public class PlayManagementController {

	@Autowired
	private TavoloService tavoloService;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private RuoloService ruoloService;

	@PutMapping("/addCredito/{credito}")
	@ResponseStatus(HttpStatus.OK)
	public void addCredito(@RequestHeader("Authorization") String user, @PathVariable(required = true) Double credito) {
		Utente utenteInSession = utenteService.findByUsername(user);

		if (utenteInSession == null || utenteInSession.getRuoli().isEmpty())
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");

		Double creditoTot = utenteInSession.getCreditoResiduo() + credito;
		utenteInSession.setCreditoResiduo(creditoTot);
		utenteService.aggiorna(utenteInSession);
	}

	@GetMapping
	public Tavolo lastGame(@RequestHeader("Authorization") String user) {
		Utente utenteInSession = utenteService.findByUsername(user);

		if (utenteInSession == null || utenteInSession.getRuoli().isEmpty())
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");

		return utenteInSession.getTavolo();
	}

	@GetMapping("/{id}")
	public Tavolo lastGame(@RequestHeader("Authorization") String user, @PathVariable(required = true) Long id) {
		Utente utenteInSession = utenteService.findByUsername(user);

		if (utenteInSession == null || utenteInSession.getRuoli().isEmpty())
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");

		if (!utenteInSession.getRuoli()
				.contains(ruoloService.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN")))
			throw new UtenteNotFoundException("Operazione non consentita, non sei admin");

		Utente TavoloByUtente = utenteService.caricaSingoloUtente(id);
		return TavoloByUtente.getTavolo();
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public void abbandonaPartita(@RequestHeader("Authorization") String user) {
		Utente utenteInSession = utenteService.findByUsername(user);

		if (utenteInSession == null || utenteInSession.getRuoli().isEmpty())
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");

		if (utenteInSession.getTavolo() == null)
			throw new TavoloNotFoundException("L'utente non appartiene a nessun tavolo");

		Double esperienzaAccumulata = utenteInSession.getEsperienzaAccumulata();
		utenteInSession.setEsperienzaAccumulata(++esperienzaAccumulata);
		utenteInSession.setTavolo(null);

		utenteService.aggiorna(utenteInSession);
	}

	@PostMapping
	public List<Tavolo> search(@RequestHeader("Authorization") String user) {
		Utente utenteInSession = utenteService.findByUsername(user);

		if (utenteInSession == null || utenteInSession.getRuoli().isEmpty())
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");

		return tavoloService.trovaTuttiPerEsperienza(utenteInSession.getEsperienzaAccumulata());
	}

	@PostMapping("/gioca/{id}")
	public ResponseEntity<String> gioca(@PathVariable(required = true) Long id,
			@RequestHeader("Authorization") String user) {
		Utente utenteInSession = utenteService.findByUsername(user);

		if (utenteInSession == null || utenteInSession.getRuoli().isEmpty())
			throw new UtenteNotFoundException("Attenzione, non sei loggato!");

		Tavolo tavolo = tavoloService.caricaSingoloTavoloConUtenti(id);
		
		if (tavolo == null)
			throw new TavoloNotFoundException("Non esiste questo tavolo");
		
		if(utenteInSession.getTavolo() == null) {
			utenteInSession.setTavolo(tavolo);
			utenteService.aggiorna(utenteInSession);
		}
		
		if(!utenteInSession.getTavolo().equals(tavolo))
			throw new GameRequirementsException("Non appartieni a questo tavolo");

		tavolo.getUtenti().add(utenteInSession);

		if (tavolo.getEsperienzaMin() > utenteInSession.getEsperienzaAccumulata())
			throw new GameRequirementsException(
					"Esperienza minima richiesta non sufficiente! Chiedi a Lorenzo di farti entrare");
		if (tavolo.getCifraMinima() > utenteInSession.getCreditoResiduo())
			throw new GameRequirementsException("Credito non sufficiente!");

		utenteInSession.setCreditoResiduo(utenteInSession.getCreditoResiduo() - tavolo.getCifraMinima());

		Double segno = Math.random();
		Integer somma = (int) (Math.random() * 1000);
		Double tot = null;
		String messaggio = null;
		System.out.println("-------- SEGNO -------\n" + segno + "\n------------- SOMMA ---------\n" + somma);

		if (segno >= 0.5) {
			segno = 1D;
			System.out.println("-------- SEGNO -------\n" + segno + "\n------------- SOMMA ---------\n" + somma);
		} else if (segno <= 0.5) {
			segno = -1D;
			System.out.println("-------- SEGNO -------\n" + segno + "\n------------- SOMMA ---------\n" + somma);
		}
		tot = segno * somma;
		utenteInSession.setCreditoAccumulato(utenteInSession.getCreditoAccumulato() + tot);

		if (tot <= 0) {
			messaggio = "Hai perso!";
			utenteInSession
					.setCreditoResiduo(utenteInSession.getCreditoResiduo() - tot);
			if (utenteInSession.getCreditoResiduo() < 0) {
				messaggio = "Hai esaurito il credito!";
				utenteInSession.setCreditoResiduo(0D);
				utenteInSession.setTavolo(null);
			}

		} else {
			messaggio = "Hai vinto!" + tot;
			utenteInSession
					.setCreditoResiduo(utenteInSession.getCreditoResiduo() + tot);
		}

		utenteService.aggiorna(utenteInSession);
		return ResponseEntity.ok(messaggio);
	}

}
