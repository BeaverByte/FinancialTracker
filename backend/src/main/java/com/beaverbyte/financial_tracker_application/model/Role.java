package com.beaverbyte.financial_tracker_application.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private RoleType name;

	public Role() {

	}

	public Role(RoleType name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RoleType getName() {
		return name;
	}

	public void setName(RoleType name) {
		this.name = name;
	}

}
