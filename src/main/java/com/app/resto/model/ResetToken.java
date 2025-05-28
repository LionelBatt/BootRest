package com.app.resto.model;

import java.util.Date;

import javax.persistence.*;

@Entity
public class ResetToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String token;
	private String email;

	private Date expiration;
	
	private boolean tokenUsed;

	public ResetToken() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public boolean isTokenUsed() {
		return tokenUsed;
	}

	public void setTokenUsed(boolean tokenUsed) {
		this.tokenUsed = tokenUsed;
	}

}
