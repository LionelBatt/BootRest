package com.app.travel.model;
import java.util.List;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "Orders")
public class Order {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int commandId;
	
	@Column(nullable = false)
	private int userId;
	
    @OneToOne @JoinColumn(name="trip_Id")
	@Column(nullable = false)
	private Trip trip;
	
	@Column(name = "number_of_passenger", nullable = false)
	private int numberOfPassagers;
	
	
	@Column(name = "options_list", nullable = false)
	private List<Option> options;

	@Column(name = "Trip_Start_Date", nullable = false)
	private Date tripStartDate;

	@Column(name = "Travel_Time", nullable = false)
	private Date travelTime;

	@Column(name = "Creation_Date", nullable = false)
	private Date creationDate;

	@Column(nullable = false)
	private double total;
	
	@Version
	private int version;

	public Order() {
	}

	

	public Order(int userId, Trip trip, int numberOfPassagers, List<Option> options, Date tripStartDate,
			Date travelTime, Date creationDate, double total) {
		this.userId = userId;
		this.trip = trip;
		this.numberOfPassagers = numberOfPassagers;
		this.options = options;
		this.tripStartDate = tripStartDate;
		this.travelTime = travelTime;
		this.creationDate = creationDate;
		this.total = total;
	}



	public int getCommandId() {
		return commandId;
	}

	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public List<Option> getOptions() {
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
		return "Order [commandId=" + commandId + ", userId=" + userId + ", trip=" + trip + ", numberOfPassagers="
				+ numberOfPassagers + ", options=" + options + ", tripStartDate=" + tripStartDate + ", travelTime="
				+ travelTime + ", creationDate=" + creationDate + ", total=" + total + "]";
	}
	
	
}
