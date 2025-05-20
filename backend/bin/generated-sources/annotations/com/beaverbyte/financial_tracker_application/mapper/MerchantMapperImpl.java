package com.beaverbyte.financial_tracker_application.mapper;

import com.beaverbyte.financial_tracker_application.dto.response.MerchantDTO;
import com.beaverbyte.financial_tracker_application.model.Merchant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-19T13:05:04-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class MerchantMapperImpl implements MerchantMapper {

    @Override
    public MerchantDTO merchantToDTO(Merchant merchant) {
        if ( merchant == null ) {
            return null;
        }

        long id = 0L;
        String name = null;

        if ( merchant.getId() != null ) {
            id = merchant.getId();
        }
        name = merchant.getName();

        MerchantDTO merchantDTO = new MerchantDTO( id, name );

        return merchantDTO;
    }
}
