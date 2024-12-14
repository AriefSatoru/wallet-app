package com.nutech.walletapp.repository.repositoryimpl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.nutech.walletapp.entity.Layanan;
import com.nutech.walletapp.repository.ServiceRepository;

@Repository
public class ServiceRepositoryImpl implements ServiceRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper untuk memetakan hasil query ke entitas Layanan
    private static class LayananRowMapper implements RowMapper<Layanan> {
        @Override
        public Layanan mapRow(ResultSet rs, int rowNum) throws SQLException {
            Layanan service = new Layanan();
            service.setId(rs.getObject("id") != null ? (Long) rs.getObject("id") : null);
            service.setServiceCode(rs.getObject("service_code") != null ? (String) rs.getObject("service_code") : null);
            service.setServiceName(rs.getObject("service_name") != null ? (String) rs.getObject("service_name") : null);
            service.setDescription(rs.getObject("description") != null ? (String) rs.getObject("description") : null);
            service.setTarif(rs.getObject("tarif") != null ? (BigDecimal) rs.getObject("tarif") : null);
            service.setCreatedAt(rs.getObject("created_at") != null ? (Date) rs.getObject("created_at") : null);
            return service;
        }
    }

    @Override
    public Layanan getServiceByCode(String code) {
        String sql = "SELECT * FROM services WHERE service_code = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new LayananRowMapper(), code);
        } catch (EmptyResultDataAccessException e) {
            return new Layanan();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user by email: " + code, e);
        }
    }

    @Override
    public List<Layanan> getListDataServices() {
        String sql = "SELECT * FROM services";
        try {
            return jdbcTemplate.query(sql, new LayananRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching layanan: ", e);
        }
    }
    
}
