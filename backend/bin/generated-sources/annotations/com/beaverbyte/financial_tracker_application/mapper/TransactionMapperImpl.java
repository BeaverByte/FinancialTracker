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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-19T13:05:05-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Transaction transactionRequestToTransaction(TransactionRequest transactionRequest, Transaction transaction) {
        if ( transactionRequest == null ) {
            return transaction;
        }

        if ( transactionRequest.id() != null ) {
            transaction.setId( transactionRequest.id() );
        }
        if ( transactionRequest.date() != null ) {
            transaction.setDate( transactionRequest.date() );
        }
        if ( transactionRequest.merchant() != null ) {
            transaction.setMerchant( merchantMapper.stringToMerchant( transactionRequest.merchant() ) );
        }
        if ( transactionRequest.account() != null ) {
            transaction.setAccount( accountMapper.stringToAccount( transactionRequest.account() ) );
        }
        if ( transactionRequest.category() != null ) {
            transaction.setCategory( categoryMapper.stringToCategory( transactionRequest.category() ) );
        }
        if ( transactionRequest.amount() != null ) {
            transaction.setAmount( transactionRequest.amount() );
        }
        if ( transactionRequest.note() != null ) {
            transaction.setNote( transactionRequest.note() );
        }

        return transaction;
    }

    @Override
    public Transaction transactionRequestToTransaction(TransactionRequest transactionRequest) {
        if ( transactionRequest == null ) {
            return null;
        }

        Transaction transaction = new Transaction();

        transaction.setMerchant( merchantMapper.stringToMerchant( transactionRequest.merchant() ) );
        transaction.setAccount( accountMapper.stringToAccount( transactionRequest.account() ) );
        transaction.setCategory( categoryMapper.stringToCategory( transactionRequest.category() ) );
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

        String merchant = null;
        String account = null;
        String category = null;
        Long id = null;
        LocalDate date = null;
        BigDecimal amount = null;
        String note = null;

        merchant = transactionMerchantName( transaction );
        account = transactionAccountName( transaction );
        category = transactionCategoryName( transaction );
        id = transaction.getId();
        date = transaction.getDate();
        amount = transaction.getAmount();
        note = transaction.getNote();

        TransactionDTO transactionDTO = new TransactionDTO( id, date, merchant, account, category, amount, note );

        return transactionDTO;
    }

    private String transactionMerchantName(Transaction transaction) {
        Merchant merchant = transaction.getMerchant();
        if ( merchant == null ) {
            return null;
        }
        return merchant.getName();
    }

    private String transactionAccountName(Transaction transaction) {
        Account account = transaction.getAccount();
        if ( account == null ) {
            return null;
        }
        return account.getName();
    }

    private String transactionCategoryName(Transaction transaction) {
        Category category = transaction.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getName();
    }
}
