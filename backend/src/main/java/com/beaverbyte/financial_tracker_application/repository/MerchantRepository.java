package com.beaverbyte.financial_tracker_application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beaverbyte.financial_tracker_application.model.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
	Optional<Merchant> findByName(String name);
}