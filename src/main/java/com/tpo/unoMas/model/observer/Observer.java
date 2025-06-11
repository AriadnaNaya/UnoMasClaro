package com.tpo.unoMas.model.observer;

import com.tpo.unoMas.model.Partido;

// La clase que espera los cambios en la clase observada para hacer algo
public interface Observer {
    void update(Partido partido);
}