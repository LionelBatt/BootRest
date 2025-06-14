package com.app.travel.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "trips")
public class Trip implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "destination_country")
    private Country destinationCountry;

    @Enumerated(EnumType.STRING)
    @Column(name = "destination_continent")
    private Continent destinationContinent;

    @Enumerated(EnumType.STRING)
    @Column(name = "destination_city")
    private City destinationCity;

    @Column(name = "minimum_duration")
    private int minimumDuration;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Order> orders;

    @ManyToMany(mappedBy = "bookmarks")
    @JsonIgnore
    private Collection<Users> lovers;

    @ManyToMany
    @JoinTable(name="trip_options", joinColumns = @JoinColumn(name = "trip_id"), inverseJoinColumns = @JoinColumn(name = "option_id"))
    private Collection<Option> packageOptions;

    private int unitPrice;

    @Version
    private int version;

    public Trip() {
        super();
    }

    public Trip(Country destinationCountry, Continent destinationContinent, City destinationCity,int minimumDuration, String description, Collection<Option> packageOptions, int unitPrice) {
        this.destinationCountry = destinationCountry;
        this.destinationContinent = destinationContinent;
        this.destinationCity = destinationCity;
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

    public Country getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(Country destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public Continent getDestinationContinent() {
        return destinationContinent;
    }

    public void setDestinationContinent(Continent destinationContinent) {
        this.destinationContinent = destinationContinent;
    }

    public City getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(City destinationCity) {
        this.destinationCity = destinationCity;
    }

    public int getMinimumDuration() {
        return minimumDuration;
    }

    public void setMinimumDuration(int minimumDuration) {
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

    @Override
    public String toString() {
        return "Trip [id=" + id + ", destinationCountry=" + destinationCountry + ", destinationContinent="
                + destinationContinent + ", destinationCity=" + destinationCity + ", minimumDuration="
                + minimumDuration + ", description=" + description + ", packageOptions=" + packageOptions
                + ", unitPrice=" + unitPrice + "]";
    }
}