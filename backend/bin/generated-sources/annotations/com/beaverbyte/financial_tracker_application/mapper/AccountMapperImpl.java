package com.beaverbyte.financial_tracker_application.mapper;

import com.beaverbyte.financial_tracker_application.dto.response.AccountDTO;
import com.beaverbyte.financial_tracker_application.model.Account;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-19T13:05:05-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public AccountDTO accountToDTO(Account account) {
        if ( account == null ) {
            return null;
        }

        long id = 0L;
        String name = null;

        if ( account.getId() != null ) {
            id = account.getId();
        }
        name = account.getName();

        AccountDTO accountDTO = new AccountDTO( id, name );

        return accountDTO;
    }
}
