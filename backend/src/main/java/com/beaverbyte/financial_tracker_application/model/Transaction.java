package com.beaverbyte.financial_tracker_application.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "date")
	private LocalDate date;

	// Merchant name (like mcdonald's, Spotify, etc.)
	@ManyToOne
	@JoinColumn(name = "merchant_id")
	private Merchant merchant;

	// Account (user generated)
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;

	// Category (like Shopping, Income, Mortgage)
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	// Amount (using Postgresql numeric(19,4))
	// Most major currencies only require 2 decimal places, but some require 4
	@Column(name = "amount", precision = 19, scale = 4)
	private BigDecimal amount;

	// Note set by user
	@Column(name = "note")
	private String note;

	public Transaction(long id, LocalDate date, Merchant merchant, Account account, Category category,
			BigDecimal amount, String note) {
		this.id = id;
		this.date = date;
		this.merchant = merchant;
		this.account = account;
		this.category = category;
		this.amount = amount;
		this.note = note;
	}

	public Transaction() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
