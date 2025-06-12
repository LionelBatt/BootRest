package com.app.travel.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
  
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private Users user;

    @Enumerated(EnumType.STRING)
    private City destination;

    private Date minimumDuration;
    private String description;
    private List<String> packageOptions;
    private int unitPrice;

    @Version
    private int version;

    public Trip() {

    }
    
    public Trip( City destination, Users user, Date minimumDuration, String description, List<String> packageOptions, int unitPrice) {
        this.user = user;
        this.destination = destination;
        this.minimumDuration = minimumDuration;
        this.description = description;
        this.packageOptions = packageOptions;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public City getDestination() {  
        return destination;    
    }
    public void setDestination(City destination) {
         this.destination = destination; 
        }

    public Users getUser() {   
        return user;   
    }
    public void setUser(Users user) {  
        this.user = user;  
    }

    public Date getMinimumDuration() { 
        return minimumDuration;   
    }

    public void setMinimumDuration(Date minimumDuration) {
        this.minimumDuration = minimumDuration; 
    }

    public String getDescription() { 
        return description; 
    }

    public void setDescription(String description) { 
        this.description = description;  
    }

    public List<String> getPackageOptions() { 
        return packageOptions;
    }
    public void setPackageOptions(List<String> packageOptions) { 
        this.packageOptions = packageOptions;
    }

    public int getUnitPrice() {   
        return unitPrice;  
    }
    public void setUnitPrice(int unitPrice) { 
        this.unitPrice = unitPrice; 
    }
    public int getVersion() { 
        return version; 
    }
    public void setVersion(int version) { 
        this.version = version; 
    }
}