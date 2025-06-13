package com.app.travel.model;

import jakarta.persistence.Column;

public class CardInfo {
    
    @Column(name = "Card_Number", nullable = false)
    private String cardNumber;

    @Column(name = "Card_Holder", nullable = false)
    private String cardHolder;

    @Column(name = "Secret_Number", nullable = false)
    private int secretNumber;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public int getSecretNumber() {
        return secretNumber;
    }

    public void setSecretNumber(int secretNumber) {
        this.secretNumber = secretNumber;
    }

    public CardInfo() {
    }

    public CardInfo(String cardNumber, String cardHolder, int secretNumber) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.secretNumber = secretNumber;
    }
}
