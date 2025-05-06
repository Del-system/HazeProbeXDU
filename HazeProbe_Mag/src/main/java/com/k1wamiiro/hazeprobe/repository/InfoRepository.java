package com.k1wamiiro.hazeprobe.repository;

import com.k1wamiiro.hazeprobe.entity.InfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InfoRepository extends JpaRepository<InfoEntity, Long> {
    Optional<InfoEntity> findByDeviceId(String deviceId);
}
