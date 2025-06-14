package com.app.travel.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "card_info")
public class CardInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_info_id")
    private int id;

    @Column(name = "card_number", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String cardNumber;

    @Column(name = "card_holder", nullable = false)
    private String cardHolder;

    @Column(name = "secret_number", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int secretNumber;



    public CardInfo() {
        super();
    }

    public CardInfo(String cardNumber, String cardHolder, int secretNumber) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.secretNumber = secretNumber;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
    
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    @Override
    public String toString() {
        return "CardInfo [id=" + id + ", cardHolder=" + cardHolder + ", cardNumber=" + getMaskedCardNumber() + "]";
    }
}
