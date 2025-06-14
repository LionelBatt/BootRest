package com.app.travel.model;

public enum Continent {
    AFRIQUE(1),
    AMERIQUE_DU_NORD(2),
    AMERIQUE_DU_SUD(3),
    ANTARTIQUE(4),
    ASIE(5),
    EUROPE(6),
    OCEANIE(7);
    
    private final int id;
    
    Continent(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public static Continent fromId(int id) {
        for (Continent continent : values()) {
            if (continent.getId() == id) {
                return continent;
            }
        }
        throw new IllegalArgumentException("Continent introuvable avec l'ID: " + id);
    }
}
