package com.beaverbyte.financial_tracker_application.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "refreshtoken")
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Column(nullable = false, unique = true)
	private String token;

	@Column(nullable = false)
	private Instant expiryDate;

	protected RefreshToken() {
		// Needed by JPA for Builder
	}

	public RefreshToken(long id, User user, String token, Instant expiryDate) {
		this.id = id;
		this.user = user;
		this.token = token;
		this.expiryDate = expiryDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Instant getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Instant expiryDate) {
		this.expiryDate = expiryDate;
	}

	public static class RefreshTokenBuilder {
		private User user;
		private String token;
		private Instant expiryDate;

		public RefreshTokenBuilder user(User user) {
			this.user = user;
			return this;
		}

		public RefreshTokenBuilder token(String token) {
			this.token = token;
			return this;
		}

		public RefreshTokenBuilder expiryDate(Instant expiryDate) {
			this.expiryDate = expiryDate;
			return this;
		}

		// id is set by JPA when persisted
		public RefreshToken build() {
			return new RefreshToken(0, user, token, expiryDate);
		}
	}

	public static RefreshTokenBuilder builder() {
		return new RefreshTokenBuilder();
	}
}
