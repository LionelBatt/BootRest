package com.app.travel.model;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "options")
public class Option {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "optionid")
    private int optionId;

    @Column(name = "description", nullable = false)
    private String desc;

    @Column(nullable = false)
    private double prix;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToMany(mappedBy = "options")
    private Collection<Order> orders;

    @ManyToMany(mappedBy = "packageOptions")
    private Collection<Trip> trips;
    
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Collection<Order> getOrders() {
        return orders;
    }

    public void setOrders(Collection<Order> orders) {
        this.orders = orders;
    }

    public Collection<Trip> getTrips() {
        return trips;
    }

    public void setTrips(Collection<Trip> trips) {
        this.trips = trips;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
