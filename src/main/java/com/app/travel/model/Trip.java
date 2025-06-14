package com.app.travel.model;

import java.io.Serializable;
import java.util.Collection;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private Country DestinationCountry;
    
    @Enumerated(EnumType.STRING)
    private Continent DestinationContinent;

    @Enumerated(EnumType.STRING)
    private City DestinationCity;

    private int minimumDuration;

    private String description;

    @OneToMany(mappedBy = "trip")
    private Collection<Order> orders;

    @ManyToMany(mappedBy = "bookmarks")
    private Collection<Users> lovers;

    @ManyToMany
    @JoinTable(name="trip_options", joinColumns = @JoinColumn(name = "trip_id"), inverseJoinColumns = @JoinColumn(name = "option_id"))
    private Collection<Option> packageOptions;

    private int unitPrice;

    @Version
    private int version;

    public Trip(Country destinationCountry, Continent destinationContinent, City destinationCity,int minimumDuration, String description, Collection<Option> packageOptions, int unitPrice) {
        this.DestinationCountry = destinationCountry;
        this.DestinationContinent = destinationContinent;
        this.DestinationCity = destinationCity;
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

    public Country getDestination_Country() {
        return DestinationCountry;
    }

    public void setDestination_Country(Country destination_Country) {
        DestinationCountry = destination_Country;
    }

    public Continent getDestination_Continent() {
        return DestinationContinent;
    }

    public void setDestination_Continent(Continent destination_Continent) {
        DestinationContinent = destination_Continent;
    }

    public City getDestination_City() {
        return DestinationCity;
    }

    public void setDestination_City(City destination_City) {
        DestinationCity = destination_City;
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
        return "Trip [id=" + id + ", Destination_Country=" + DestinationCountry + ", Destination_Continent="
                + DestinationContinent + ", Destination_City=" + DestinationCity + ", minimumDuration="
                + minimumDuration + ", description=" + description + ", packageOptions=" + packageOptions
                + ", unitPrice=" + unitPrice + "]";
    }
}