package com.app.travel.model;

import java.util.Collection;
import java.util.Date;

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
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private Country Destination_Country;
    
    @Enumerated(EnumType.STRING)
    private Continent Destination_Continent;

    @Enumerated(EnumType.STRING)
    private City Destination_City;

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

    public Trip(Country destination_Country, Continent destination_Continent, City destination_City,
            Date minimumDuration, String description, Collection<Option> packageOptions, int unitPrice) {
        Destination_Country = destination_Country;
        Destination_Continent = destination_Continent;
        Destination_City = destination_City;
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
        return Destination_Country;
    }

    public void setDestination_Country(Country destination_Country) {
        Destination_Country = destination_Country;
    }

    public Continent getDestination_Continent() {
        return Destination_Continent;
    }

    public void setDestination_Continent(Continent destination_Continent) {
        Destination_Continent = destination_Continent;
    }

    public City getDestination_City() {
        return Destination_City;
    }

    public void setDestination_City(City destination_City) {
        Destination_City = destination_City;
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

    @Override
    public String toString() {
        return "Trip [id=" + id + ", Destination_Country=" + Destination_Country + ", Destination_Continent="
                + Destination_Continent + ", Destination_City=" + Destination_City + ", minimumDuration="
                + minimumDuration + ", description=" + description + ", packageOptions=" + packageOptions
                + ", unitPrice=" + unitPrice + "]";
    }
}