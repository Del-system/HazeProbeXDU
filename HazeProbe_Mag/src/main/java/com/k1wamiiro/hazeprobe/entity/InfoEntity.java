package com.k1wamiiro.hazeprobe.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@Table(name = "user_info")
@DynamicUpdate
public class InfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String deviceId;

    private String province;
    private String city;
    private String district;
    private double latitude;
    private double longitude;
    private String temp;
    private String weatherInfo;
    private String aqi;
    private String pm25;
    private String pm10;
}
