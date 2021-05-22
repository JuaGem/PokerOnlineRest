package com.projectpokerrest.pokerrest.repository.tavolo;

import com.projectpokerrest.pokerrest.model.Tavolo;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomTavoloRepositoryImpl implements CustomTavoloRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tavolo> findByExample(Tavolo example) {

        Map<String, Object> paramaterMap = new HashMap<String, Object>();
        List<String> whereClauses = new ArrayList<String>();

        StringBuilder queryBuilder = new StringBuilder("select DISTINCT t from Tavolo t left join fetch t.utenti u where t.id = t.id ");

        if (StringUtils.isNotEmpty(example.getDenominazione())) {
            whereClauses.add(" t.denominazione like :denominazione ");
            paramaterMap.put("denominazione", "%" + example.getDenominazione() + "%");
        }
        if (example.getCifraMinima() != null) {
            whereClauses.add(" t.cifraMinima >= :cifraMinima ");
            paramaterMap.put("cifraMinima", example.getCifraMinima());
        }
        if (example.getEsperienzaMin() != null) {
            whereClauses.add(" t.esperienzaMin >= :esperienzaMin ");
            paramaterMap.put("esperienzaMin", example.getEsperienzaMin());
        }
        if (example.getDataCreazione() != null) {
            whereClauses.add(" t.dataCreazione >= :dataCreazione ");
            paramaterMap.put("dataCreazione", example.getDataCreazione());
        }
        if (example.getDataCreazione() != null) {
            whereClauses.add(" t.utenteCreazione >= :utenteCreazione ");
            paramaterMap.put("utenteCreazione", example.getUtenteCreazione());
        }
        if (example.getUtenti() != null && !example.getUtenti().isEmpty()) {
            whereClauses.add("c.id in :idList ");
            paramaterMap.put("idList", example.getUtenti().stream().map(utenti -> utenti.getId()).collect(Collectors.toList()));
        }

        queryBuilder.append(!whereClauses.isEmpty()?" and ":"");
        queryBuilder.append(StringUtils.join(whereClauses, " and "));
        TypedQuery<Tavolo> typedQuery = entityManager.createQuery(queryBuilder.toString(), Tavolo.class);

        for (String key : paramaterMap.keySet()) {
            typedQuery.setParameter(key, paramaterMap.get(key));
        }

        return typedQuery.getResultList();

    }

}
