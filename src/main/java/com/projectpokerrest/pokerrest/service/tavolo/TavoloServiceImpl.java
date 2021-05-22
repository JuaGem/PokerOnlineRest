package com.projectpokerrest.pokerrest.service.tavolo;

import com.projectpokerrest.pokerrest.model.Tavolo;
import com.projectpokerrest.pokerrest.model.Utente;
import com.projectpokerrest.pokerrest.repository.tavolo.TavoloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class TavoloServiceImpl implements TavoloService {

    @Autowired
    private TavoloRepository repository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Tavolo> listAllTavolo() {
        return (List<Tavolo>) repository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Tavolo> listAllEager() {
        return repository.findAllEager();
    }

    @Transactional(readOnly = true)
    public Tavolo caricaSingoloTavolo(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Tavolo caricaSingoloTavoloConUtenti(Long id) {
        return repository.findOneEager(id);
    }

    @Transactional
    public Tavolo aggiorna(Tavolo tavoloInstance) {
 	tavoloInstance = entityManager.merge(tavoloInstance);
    	
    	if(!tavoloInstance.getUtenti().isEmpty())
    		throw new RuntimeException("Tavolo occupato da giocatori, impossibile modificarlo");
    	
        return repository.save(tavoloInstance);
    }

    @Transactional
    public Tavolo inserisciNuovo(Tavolo tavoloInstance) {
        return repository.save(tavoloInstance);
    }

    @Transactional
    public void rimuovi(Tavolo tavoloInstance) {
    	tavoloInstance = entityManager.merge(tavoloInstance);
    	
    	if(!tavoloInstance.getUtenti().isEmpty())
    		throw new RuntimeException("Tavolo occupato da giocatori, impossibile eliminarlo");
    	
        repository.delete(tavoloInstance);
    }

    @Transactional(readOnly = true)
    public List<Tavolo> findByExample(Tavolo tavoloInstance) {
        return repository.findByExample(tavoloInstance);
    }
    
    @Transactional(readOnly = true)
    public List<Utente> listAllByTavolo(Long id) {
    	return repository.findAllByTavolo(id);
    }

}
