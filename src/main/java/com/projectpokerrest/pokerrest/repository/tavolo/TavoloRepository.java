package com.projectpokerrest.pokerrest.repository.tavolo;

import com.projectpokerrest.pokerrest.model.Tavolo;
import com.projectpokerrest.pokerrest.model.Utente;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TavoloRepository extends CrudRepository<Tavolo, Long>, CustomTavoloRepository {

    @Query("from Tavolo t left join fetch t.utenti u where t.id = :id")
    Tavolo findOneEager(Long id);
    
    @Query("select t from Tavolo t left join fetch t.utenteCreazione c")
    List<Tavolo> findAllEager();
    
    @Query("select u from Tavolo t left join fetch t.utenti u where t.id = ?1")
    List<Utente> findAllByTavolo(Long id);

}
