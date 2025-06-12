package com.app.travel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "users")
public class Users {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	
	@Column(name = "user_name", nullable = false, unique = true)
	private String userName;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(name = "phone_number", nullable = true, unique = true)
	private String phoneNumber;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String surname;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private role role;
	
	@Version
	private int version;

	public Users() {
	}
	
	public Users(String userName, String password, String email, String phoneNumber, String name, String surname,
			String address, com.app.travel.model.role role) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.surname = surname;
		this.address = address;
		this.role = role;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public role getRole() {
		return role;
	}

	public void setRole(role role) {
		this.role = role;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Users [userId=" + userId + ", userName=" + userName + ", password=" + password + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + ", name=" + name + ", surname=" + surname + ", address=" + address
				+ ", role=" + role + "]";
	}
	
}
