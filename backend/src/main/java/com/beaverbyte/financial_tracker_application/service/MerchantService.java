package com.beaverbyte.financial_tracker_application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.dto.response.MerchantDTO;
import com.beaverbyte.financial_tracker_application.mapper.MerchantMapper;
import com.beaverbyte.financial_tracker_application.repository.MerchantRepository;

@Service
public class MerchantService {
	private final MerchantRepository merchantRepository;
	private final MerchantMapper merchantMapper;

	public MerchantService(MerchantRepository merchantRepository, MerchantMapper merchantMapper) {
		this.merchantRepository = merchantRepository;
		this.merchantMapper = merchantMapper;
	}

	public List<MerchantDTO> findAll() {
		return merchantRepository.findAll()
				.stream()
				.map(merchantMapper::merchantToDTO)
				.toList();
	}
}
