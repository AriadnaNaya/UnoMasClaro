package com.tpo.unoMas.model.observer;

// La clase que va a notificar de sus cambios de estado a los que Observan
public interface Observable {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers();
}