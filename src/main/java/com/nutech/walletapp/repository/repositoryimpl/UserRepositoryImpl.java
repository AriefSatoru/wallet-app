package com.nutech.walletapp.repository.repositoryimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.nutech.walletapp.entity.User;
import com.nutech.walletapp.model.dtoresponse.CekSaldoResponse;
import com.nutech.walletapp.repository.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper untuk memetakan hasil query ke entitas User
    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getLong("user_id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setProfileImageUrl(rs.getString("profile_image_url"));
            user.setCreatedAt(rs.getTimestamp("created_at"));
            user.setUpdatedAt(rs.getTimestamp("updated_at"));
            return user;
        }
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            return new User();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user by email: " + email, e);
        }
    }

    @Override
    public List<User> findAllUsers() {
        String sql = "SELECT first_name, last_name, email, FROM users";
        try {
            return jdbcTemplate.query(sql, new UserRowMapper());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all users", e);
        }
    }

    @Override
    public void saveUser(User user) {
        String sql = "INSERT INTO users (first_name, last_name, email, password_hash, phone_number, profile_image_url, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, 
                user.getFirstName(),
                user.getLastName(),    
                user.getEmail(),
                user.getPasswordHash(),
                user.getPhoneNumber(),
                user.getProfileImageUrl(),
                user.getCreatedAt(),
                user.getUpdatedAt()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + user.getEmail(), e);
        }
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users " +
                     "SET first_name = ?, " +
                     "    last_name = ? "+
                     "WHERE email = ?";
        try {
            jdbcTemplate.update(sql, 
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + user.getEmail(), e);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public Boolean exsistByEmail(String email) {
        String sql = "SELECT EXISTS (SELECT email FROM users WHERE email = ?)";
        try {
            return jdbcTemplate.queryForObject(sql, Boolean.class, email);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user by email: " + email, e);
        }
    }

    @Override
    public CekSaldoResponse getSaldoByEmail(String email) {
        String sql = "SELECT saldo FROM users u join balances b on u.user_id = b.user_id WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, CekSaldoResponse.class, email);
        } catch (EmptyResultDataAccessException e) {
            return new CekSaldoResponse();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching saldo by email: " + email, e);
        }
    }

    @Override
    public void updateUserImage(User user) {
        String sql = "UPDATE users " +
                     "SET profile_image_url = ? " +
                     "WHERE email = ?";
        try {
            jdbcTemplate.update(sql, 
                user.getProfileImageUrl(),
                user.getEmail()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + user.getEmail(), e);
        }
    }
    
}
