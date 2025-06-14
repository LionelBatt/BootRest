package com.app.travel.model;

public enum Country {
    // EUROPE
    FRANCE(1),
    ITALY(2),
    SPAIN(3),
    GREECE(4),
    PORTUGAL(5),
    UNITED_KINGDOM(6),
    NETHERLANDS(7),
    GERMANY(8),
    
    // ASIE
    JAPAN(9),
    THAILAND(10),
    INDIA(11),
    CHINA(12),
    VIETNAM(13),
    SOUTH_KOREA(14),
    SINGAPORE(15),
    
    // AMERIQUE_DU_NORD
    USA(16),
    CANADA(17),
    
    // AMERIQUE_DU_SUD
    BRAZIL(18),
    ARGENTINA(19),
    PERU(20),
    CHILE(21),
    
    // AFRIQUE
    MOROCCO(22),
    EGYPT(23),
    SOUTH_AFRICA(24),
    KENYA(25),
    TUNISIA(26),
    
    // OCEANIE
    AUSTRALIA(27),
    NEW_ZEALAND(28),
    FIJI(29);
    
    private final int id;
    
    Country(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public static Country fromId(int id) {
        for (Country country : values()) {
            if (country.getId() == id) {
                return country;
            }
        }
        throw new IllegalArgumentException("Pays introuvable avec l'ID: " + id);
    }
}