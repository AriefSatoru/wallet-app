package com.nutech.walletapp.repository.repositoryimpl;

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

import com.nutech.walletapp.entity.Banner;
import com.nutech.walletapp.repository.BannerRepository;

@Repository
public class BannerRepositoryImpl implements BannerRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static class BannerRowMapper implements RowMapper<Banner> {

        @Override
        public Banner mapRow(ResultSet rs, int rowNum) throws SQLException {
            Banner banner = new Banner();
            banner.setId(rs.getObject("id") != null ? (Long) rs.getObject("id") : null);
            banner.setBannerName(rs.getObject("banner_name") != null ? (String) rs.getObject("banner_name") : null);
            banner.setBannerImage(rs.getObject("banner_image") != null ? (String) rs.getObject("banner_image") : null);
            banner.setDescription(rs.getObject("description") != null ? (String) rs.getObject("description") : null);
            banner.setCreatedAt(rs.getObject("created_at") != null ? (Date) rs.getObject("created_at") : null);
            banner.setUpdatedAt(rs.getObject("updated_at") != null ? (Date) rs.getObject("updated_at") : null);
            return banner;
        }

    }

    @Override
    public List<Banner> getListDataBanners() {
        String sql = "SELECT * FROM banners";
        try {
            return jdbcTemplate.query(sql, new BannerRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching banner: ", e);
        }
    }

    
    
}
