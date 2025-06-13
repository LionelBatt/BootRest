package com.app.travel.model;
import java.sql.Date;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderId;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private Users user;
	
    @ManyToOne
	@JoinColumn(name="trip_id", nullable = false)
	private Trip trip;
	
	@Column(name = "number_of_passenger", nullable = false)
	private int numberOfPassagers;
	
	@ManyToMany
	@JoinTable(name="order_options", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "option_id"))
	private Collection<Option> options;

	@Column(name = "trip_start_date", nullable = false)
	private Date tripStartDate;

	@Column(name = "travel_time", nullable = false)
	private Date travelTime;

	@Column(name = "creation_date", nullable = false)
	private Date creationDate;

	@Column(nullable = false)
	private double total;
	
	@Version
	private int version;

	public Order() {
		super();
	}

	public Order(Users user, Trip trip, int numberOfPassagers, List<Option> options, Date tripStartDate,Date travelTime, Date creationDate, double total) {
		this.user = user;
		this.trip = trip;
		this.numberOfPassagers = numberOfPassagers;
		this.options = options;
		this.tripStartDate = tripStartDate;
		this.travelTime = travelTime;
		this.creationDate = creationDate;
		this.total = total;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public int getNumberOfPassagers() {
		return numberOfPassagers;
	}

	public void setNumberOfPassagers(int numberOfPassagers) {
		this.numberOfPassagers = numberOfPassagers;
	}

	public Collection<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public Date getTripStartDate() {
		return tripStartDate;
	}

	public void setTripStartDate(Date tripStartDate) {
		this.tripStartDate = tripStartDate;
	}

	public Date getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(Date travelTime) {
		this.travelTime = travelTime;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", user=" + user.getUsername() + ", trip=" + trip + ", numberOfPassagers="
				+ numberOfPassagers + ", options=" + options + ", tripStartDate=" + tripStartDate + ", travelTime="
				+ travelTime + ", creationDate=" + creationDate + ", total=" + total + "]";
	}

}
