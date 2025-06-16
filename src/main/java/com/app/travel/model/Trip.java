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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;

@Entity
@Table(name = "trips")
public class Trip implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Correction: Noms des champs cohérents avec les getters/setters
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

    @ManyToMany(fetch = FetchType.EAGER)
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

    // === GETTERS ET SETTERS CORRIGÉS ===
    
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

    // === MÉTHODES UTILITAIRES ===
    
    /**
     * Vérifie si le voyage a des commandes
     */
    public boolean hasOrders() {
        return orders != null && !orders.isEmpty();
    }
    
    /**
     * Vérifie si le voyage est dans les favoris
     */
    public boolean hasFavorites() {
        return lovers != null && !lovers.isEmpty();
    }
    
    /**
     * Obtient le nombre de commandes
     */
    public int getOrderCount() {
        return orders != null ? orders.size() : 0;
    }
    
    /**
     * Obtient le nombre de favoris
     */
    public int getFavoriteCount() {
        return lovers != null ? lovers.size() : 0;
    }
    
    /**
     * Obtient le nombre d'options
     */
    public int getOptionCount() {
        return packageOptions != null ? packageOptions.size() : 0;
    }

    // === MÉTHODES D'AFFICHAGE ===
    
    /**
     * Retourne une représentation textuelle de la destination complète
     */
    public String getFullDestination() {
        return String.format("%s, %s, %s", 
            destinationCity != null ? destinationCity.name() : "N/A",
            destinationCountry != null ? destinationCountry.name() : "N/A",
            destinationContinent != null ? destinationContinent.name() : "N/A"
        );
    }
    
    /**
     * Retourne une représentation formatée du prix
     */
    public String getFormattedPrice() {
        return String.format("%d€", unitPrice);
    }

    @Override
    public String toString() {
        return "Trip [" +
                "id=" + id +
                ", destinationCountry=" + destinationCountry +
                ", destinationContinent=" + destinationContinent +
                ", destinationCity=" + destinationCity +
                ", minimumDuration=" + minimumDuration +
                ", description='" + description + '\'' +
                ", unitPrice=" + unitPrice +
                ", orderCount=" + getOrderCount() +
                ", favoriteCount=" + getFavoriteCount() +
                ", optionCount=" + getOptionCount() +
                ']';
    }

    // === MÉTHODES EQUALS ET HASHCODE ===
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Trip trip = (Trip) obj;
        return id == trip.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}