package com.projectpokerrest.pokerrest.repository.utente;

import com.projectpokerrest.pokerrest.model.Utente;

import java.util.List;

public interface CustomUtenteRepository {

	List<Utente> findByExample(Utente example);
	
	public Long countByUtenteAdminAttivo();
	
}
