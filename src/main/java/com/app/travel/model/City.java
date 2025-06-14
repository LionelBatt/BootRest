package com.app.travel.model;

public enum City {
    // EUROPE
    PARIS(1),
    ROME(2),
    BARCELONA(3),
    SANTORINI(4),
    LISBON(5),
    LONDON(6),
    AMSTERDAM(7),
    BERLIN(8),
    
    // ASIE
    TOKYO(9),
    BANGKOK(10),
    DELHI(11),
    BEIJING(12),
    HANOI(13),
    SEOUL(14),
    SINGAPORE(15),
    
    // AMERIQUE_DU_NORD
    NEW_YORK(16),
    MONTREAL(17),
    LOS_ANGELES(18),
    VANCOUVER(19),
    
    // AMERIQUE_DU_SUD
    RIO_DE_JANEIRO(20),
    BUENOS_AIRES(21),
    CUSCO(22),
    SANTIAGO(23),
    
    // AFRIQUE
    MARRAKECH(24),
    CAIRO(25),
    CAPE_TOWN(26),
    NAIROBI(27),
    TUNIS(28),
    
    // OCEANIE
    SYDNEY(29),
    AUCKLAND(30),
    SUVA(31);
    
    private final int id;
    
    City(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public static City fromId(int id) {
        for (City city : values()) {
            if (city.getId() == id) {
                return city;
            }
        }
        throw new IllegalArgumentException("Ville introuvable avec l'ID: " + id);
    }
}