package com.beaverbyte.financial_tracker_application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.beaverbyte.financial_tracker_application.dto.response.MerchantDTO;
import com.beaverbyte.financial_tracker_application.model.Merchant;

@Mapper(componentModel = "spring")
public interface MerchantMapper {
	MerchantMapper INSTANCE = Mappers.getMapper(MerchantMapper.class);

	MerchantDTO merchantToDTO(Merchant merchant);

	default Merchant stringToMerchant(String name) {
		Merchant merchant = new Merchant();
		merchant.setName(name);
		return merchant;
	}

}
