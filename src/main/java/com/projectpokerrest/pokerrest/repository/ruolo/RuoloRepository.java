package com.projectpokerrest.pokerrest.repository.ruolo;

import com.projectpokerrest.pokerrest.model.Ruolo;
import org.springframework.data.repository.CrudRepository;

public interface RuoloRepository extends CrudRepository<Ruolo, Long> {

	Ruolo findByDescrizioneAndCodice(String descrizione, String codice);
	
}
