package com.app.travel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "options")
public class Option {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int optionId;

    @Column(name = "Description", nullable = false)
    private String desc;

    @Column(nullable = false)
    private double prix;

    @Version
    private int version;

    public Option(){
        super();
    }

    public Option(String desc, double prix){
        super();
        this.desc = desc;
        this.prix = prix;
    }

    public int getOptionId(){
        return this.optionId;
    }

    public void setOptionId(int optionId){
        this.optionId = optionId;
    }

    public String getDesc(){
        return this.desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public double getPrix(){
        return this.prix;
    }

    public void setPrix(double prix){
        this.prix = prix;
    }

    public int getVersion() {
        return version;
    }
}
