package com.nutech.walletapp.repository.repositoryimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nutech.walletapp.entity.Transaction;
import com.nutech.walletapp.repository.TransactionRepository;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(Transaction transaction) {
        String sql = "INSERT INTO transactions (user_id, invoice_number, service_id, amount, transaction_type, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try {
                jdbcTemplate.update(sql, 
                transaction.getUserId(),
                transaction.getInvoiceNumber(),    
                transaction.getServiceId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getCreatedAt()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error saving transaction: " + transaction.getInvoiceNumber(), e);
        }
    }

    @Override
    public int incrementValue() {
        String sql = "SELECT nextval('invoice_seq')";

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error fetching next value from sequence", e);
        }
    }
    
}
