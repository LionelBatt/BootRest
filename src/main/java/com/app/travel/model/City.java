package com.app.travel.model;

public enum City {
    // EUROPE
    PARIS(110),         // France
    LYON(111),          // France
    MARSEILLE(112),     // France
    STRASBOURG(113),    // France
    LISBON(120),        // Portugal
    ROME(130),          // Italie
    BARCELONA(140),     // Espagne
    MADRID(141),        // Espagne
    VALENCE(142),       // Espagne
    SANTORINI(150),     // Grèce
    LONDON(160),        // Royaume-Uni
    AMSTERDAM(170),     // Pays-Bas
    BERLIN(180),        // Allemagne

    // ASIE
    TOKYO(210),         // Japon
    BANGKOK(220),       // Thaïlande
    DELHI(230),         // Inde
    BEIJING(240),       // Chine
    HANOI(250),         // Vietnam
    SEOUL(260),         // Corée du Sud
    SINGAPORE(270),     // Singapour

    // AMERIQUE_DU_NORD
    NEW_YORK(310),      // USA
    LOS_ANGELES(311),   // USA
    MONTREAL(320),      // Canada
    VANCOUVER(321),     // Canada

    // AMERIQUE_DU_SUD
    RIO_DE_JANEIRO(410),// Brésil
    BUENOS_AIRES(420),  // Argentine
    CUSCO(430),         // Pérou
    SANTIAGO(440),      // Chili

    // AFRIQUE
    MARRAKECH(510),     // Maroc
    TUNIS(511),         // Tunisie
    CAIRO(520),         // Égypte
    NAIROBI(530),       // Kenya
    CAPE_TOWN(540),     // Afrique du Sud

    // OCEANIE
    SYDNEY(610),        // Australie
    AUCKLAND(620),      // Nouvelle-Zélande
    SUVA(630);          // Fidji
    
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