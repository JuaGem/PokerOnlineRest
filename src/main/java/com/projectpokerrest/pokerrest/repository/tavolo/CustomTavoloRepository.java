package com.projectpokerrest.pokerrest.repository.tavolo;

import com.projectpokerrest.pokerrest.model.Tavolo;

import java.util.List;

public interface CustomTavoloRepository {

    List<Tavolo> findByExample(Tavolo example);

}
