package com.beaverbyte.financial_tracker_application.mapper;

import com.beaverbyte.financial_tracker_application.dto.request.TransactionRequest;
import com.beaverbyte.financial_tracker_application.dto.response.TransactionDTO;
import com.beaverbyte.financial_tracker_application.model.Account;
import com.beaverbyte.financial_tracker_application.model.Category;
import com.beaverbyte.financial_tracker_application.model.Merchant;
import com.beaverbyte.financial_tracker_application.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T16:09:11-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.z20250331-1358, environment: Java 21.0.6 (Eclipse Adoptium)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public Transaction transactionRequestToTransaction(TransactionRequest transactionRequest) {
        if ( transactionRequest == null ) {
            return null;
        }

        Transaction transaction = new Transaction();

        if ( transactionRequest.id() != null ) {
            transaction.setId( transactionRequest.id() );
        }
        transaction.setDate( transactionRequest.date() );
        transaction.setAmount( transactionRequest.amount() );
        transaction.setNote( transactionRequest.note() );

        return transaction;
    }

    @Override
    public TransactionDTO transactionToTransactionDTO(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        Long id = null;
        LocalDate date = null;
        Merchant merchant = null;
        Account account = null;
        Category category = null;
        BigDecimal amount = null;
        String note = null;

        id = transaction.getId();
        date = transaction.getDate();
        merchant = transaction.getMerchant();
        account = transaction.getAccount();
        category = transaction.getCategory();
        amount = transaction.getAmount();
        note = transaction.getNote();

        TransactionDTO transactionDTO = new TransactionDTO( id, date, merchant, account, category, amount, note );

        return transactionDTO;
    }
}
