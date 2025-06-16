package com.app.travel.model;

public enum Country {
    // EUROPE
    FRANCE(11),
    ITALY(13),
    SPAIN(14),
    GREECE(15),
    PORTUGAL(12),
    UNITED_KINGDOM(16),
    NETHERLANDS(17),
    GERMANY(18),
    
    // ASIE
    JAPAN(21),
    THAILAND(22),
    INDIA(23),
    CHINA(24),
    VIETNAM(25),
    SOUTH_KOREA(26),
    SINGAPORE(27),
    
    // AMERIQUE_DU_NORD
    USA(31),
    CANADA(32),
    
    // AMERIQUE_DU_SUD
    BRAZIL(41),
    ARGENTINA(42),
    PERU(43),
    CHILE(44),
    
    // AFRIQUE
    MOROCCO(51),
    EGYPT(52),
    SOUTH_AFRICA(53),
    KENYA(54),
    TUNISIA(55),
    
    // OCEANIE
    AUSTRALIA(61),
    NEW_ZEALAND(62),
    FIJI(63);
    
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