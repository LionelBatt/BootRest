package com.app.travel.model;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private city destination;

    private Date minimumDuration;

    private String description;

    @OneToMany(mappedBy = "trip")
    private Collection<Order> orders;

    @ManyToMany(mappedBy = "bookmarks")
    private Collection<Users> lovers;

    @ManyToMany
    @JoinTable(name="Trip_s_Options", joinColumns = @JoinColumn(name = "Trip_ID"), inverseJoinColumns = @JoinColumn(name = "Option_ID"))
    private Collection<Option> packageOptions;

    private int unitPrice;

    @Version
    private int version;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public city getDestination() {
        return destination;
    }

    public void setDestination(city destination) {
        this.destination = destination;
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

    public Collection<Option> getPackageOptions() {
        return packageOptions;
    }

    public void setPackageOptions(Collection<Option> packageOptions) {
        this.packageOptions = packageOptions;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public Collection<Order> getOrders() {
        return orders;
    }

    public void setOrders(Collection<Order> orders) {
        this.orders = orders;
    }

    public Collection<Users> getLovers() {
        return lovers;
    }

    public void setLovers(Collection<Users> lovers) {
        this.lovers = lovers;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Trip(city destination, Date minimumDuration, String description, Collection<Option> packageOptions,
            int unitPrice) {
        this.destination = destination;
        this.minimumDuration = minimumDuration;
        this.description = description;
        this.packageOptions = packageOptions;
        this.unitPrice = unitPrice;
    }
    
}