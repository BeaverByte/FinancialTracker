package com.beaverbyte.financial_tracker_application.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.beaverbyte.financial_tracker_application.dto.response.MerchantDTO;
import com.beaverbyte.financial_tracker_application.mapper.MerchantMapper;
import com.beaverbyte.financial_tracker_application.model.Merchant;
import com.beaverbyte.financial_tracker_application.repository.MerchantRepository;

@ExtendWith(MockitoExtension.class)
class MerchantServiceTest {
	@Mock
	private MerchantRepository merchantRepository;
	@Mock
	private MerchantMapper merchantMapper;
	@InjectMocks
	private MerchantService merchantService;

	@Test
	void shouldShowListIfMerchantsExist() {
		List<Merchant> merchants = new ArrayList<>(List.of(new Merchant(1L, "string")));
		List<MerchantDTO> expectedDTOs = merchants.stream()
				.map(merchant -> new MerchantDTO(merchant.getId(), merchant.getName())).toList();

		Mockito.when(merchantRepository.findAll()).thenReturn(merchants);
		Mockito.when(merchantMapper.merchantToDTO(merchants.get(0))).thenReturn(expectedDTOs.get(0));

		List<MerchantDTO> result = merchantService.findAll();

		Assertions.assertFalse(result.isEmpty());
	}
}