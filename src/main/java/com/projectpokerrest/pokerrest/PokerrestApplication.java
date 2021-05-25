package com.projectpokerrest.pokerrest;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.projectpokerrest.pokerrest.model.Ruolo;
import com.projectpokerrest.pokerrest.model.StatoUtente;
import com.projectpokerrest.pokerrest.model.Utente;
import com.projectpokerrest.pokerrest.service.ruolo.RuoloService;
import com.projectpokerrest.pokerrest.service.utente.UtenteService;

@SpringBootApplication
public class PokerrestApplication implements CommandLineRunner {

	@Autowired
	private RuoloService ruoloServiceInstance;
	@Autowired
	private UtenteService utenteServiceInstance;

	public static void main(String[] args) {
		SpringApplication.run(PokerrestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN") == null)
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Administrator", "ROLE_ADMIN"));

		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Player User", "ROLE_PLAYER") == null)
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Player User", "ROLE_PLAYER"));

		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Special Player User", "ROLE_SPECIAL_PLAYER") == null)
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Special Player User", "ROLE_SPECIAL_PLAYER"));

		if (utenteServiceInstance.findByUsernameAndPassword("admin", "admin") == null) {
			Utente admin = new Utente("admin", "admin", "Giovanni", "Gemini", new Date());
			admin.setStato(StatoUtente.ATTIVO);
			admin.getRuoli().add(ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN"));
			utenteServiceInstance.inserisciNuovo(admin);
//			utenteServiceInstance.aggiungiRuolo(admin,
//					ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN"));
		}

		if (utenteServiceInstance.findByUsernameAndPassword("user", "user") == null) {
			Utente player = new Utente("user", "user", "Antonio", "Verdi", new Date());
			player.setStato(StatoUtente.ATTIVO);
			player.getRuoli().add(ruoloServiceInstance.cercaPerDescrizioneECodice("Player User", "ROLE_PLAYER"));
			utenteServiceInstance.inserisciNuovo(player);
//			utenteServiceInstance.aggiungiRuolo(player,
//					ruoloServiceInstance.cercaPerDescrizioneECodice("Player User", "ROLE_PLAYER"));
		}

		if (utenteServiceInstance.findByUsernameAndPassword("special", "special") == null) {
			Utente specialPlayer = new Utente("special", "special", "Andrea", "Vecchiato", new Date());
			specialPlayer.setStato(StatoUtente.ATTIVO);
			specialPlayer.getRuoli()
					.add(ruoloServiceInstance.cercaPerDescrizioneECodice("Special Player User", "ROLE_SPECIAL_PLAYER"));
			utenteServiceInstance.inserisciNuovo(specialPlayer);
//			utenteServiceInstance.aggiungiRuolo(specialPlayer, 
//					ruoloServiceInstance.cercaPerDescrizioneECodice("Special Player User", "ROLE_SPECIAL_PLAYER"));
		}

	}

}
