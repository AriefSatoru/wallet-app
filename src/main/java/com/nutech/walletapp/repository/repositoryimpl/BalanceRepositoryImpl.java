package com.nutech.walletapp.repository.repositoryimpl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.nutech.walletapp.entity.Balance;
import com.nutech.walletapp.repository.BalanceRepository;

@Repository
public class BalanceRepositoryImpl implements BalanceRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper untuk memetakan hasil query ke entitas Balance
    private static class BalanceRowMapper implements RowMapper<Balance> {
        @Override
        public Balance mapRow(ResultSet rs, int rowNum) throws SQLException {
            Balance balance = new Balance();
            balance.setId(rs.getObject("id") != null ? (Long) rs.getObject("id") : null);
            balance.setEmail(rs.getObject("email") != null ? (String) rs.getObject("email") : null);
            balance.setUserId(rs.getObject("user_id") != null ? (Long) rs.getObject("user_id") : null);
            balance.setSaldo(rs.getObject("saldo") != null ? (BigDecimal) rs.getObject("saldo") : null);
            return balance;
        }
    }

    @Override
    public Balance getDataBalance(String email) {
        String sql = "SELECT b.*, u.email FROM balances b join users u " +
                     "on b.user_id = u.user_id WHERE u.email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BalanceRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            return new Balance();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user by email: " + email, e);
        }
    }

    @Override
    public void save(Balance balance) {
        String sql = "INSERT INTO balances (user_id, saldo) " +
                     "VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, 
                balance.getUserId(),
                balance.getSaldo()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + balance.getEmail(), e);
        }
    }

    @Override
    public void topUpSaldoByEmail(Balance balance) {
        String sql = "UPDATE balances " +
                     "SET saldo = ? " +
                     "WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, 
                balance.getSaldo(),
                balance.getUserId()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + balance.getEmail(), e);
        }
    }
    
}
