package com.app.travel.model;

public enum Continent {
    AFRIQUE(5),
    AMERIQUE_DU_NORD(3),
    AMERIQUE_DU_SUD(4),
    ASIE(2),
    EUROPE(1),
    OCEANIE(6);
    
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
