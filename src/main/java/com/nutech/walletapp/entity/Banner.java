package com.nutech.walletapp.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "banners")
public class Banner {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "banner_name", nullable = false)
    private String bannerName;

    @Column(name = "banner_image", nullable = false)
    private String bannerImage; 

    @Column(name = "description")
    private String description; 

    @Column(name = "created_at", updatable = false)
    private Date createdAt; 

    @Column(name = "updated_at")
    private Date updatedAt;
}
