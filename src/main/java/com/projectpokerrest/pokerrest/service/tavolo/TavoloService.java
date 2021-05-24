package com.projectpokerrest.pokerrest.service.tavolo;

import com.projectpokerrest.pokerrest.model.Tavolo;
import com.projectpokerrest.pokerrest.model.Utente;

import java.util.List;

public interface TavoloService {

    List<Tavolo> listAllTavolo() ;

    Tavolo caricaSingoloTavolo(Long id);

    Tavolo caricaSingoloTavoloConUtenti(Long id);

    Tavolo aggiorna(Tavolo tavoloInstance);

    Tavolo inserisciNuovo(Tavolo tavoloInstance);

    void rimuovi(Tavolo tavoloInstance);

    List<Tavolo> findByExample(Tavolo tavoloInstance);
    
    public List<Tavolo> listAllEager();
    
    public List<Utente> listAllByTavolo(Long id);
    
    public List<Tavolo> trovaTuttiPerEsperienza(Double esperienza);

}
