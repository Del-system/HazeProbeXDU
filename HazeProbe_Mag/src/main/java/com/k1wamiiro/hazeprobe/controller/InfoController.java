package com.k1wamiiro.hazeprobe.controller;

import com.k1wamiiro.hazeprobe.entity.InfoEntity;
import com.k1wamiiro.hazeprobe.entity.InfoResponse;
import com.k1wamiiro.hazeprobe.repository.InfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/info")
public class InfoController {
    @Autowired
    private InfoRepository infoRepository;

    @PostMapping
    public ResponseEntity<InfoResponse<Void>> saveOrUpdateInfo(@RequestBody InfoEntity infoEntity) {
        Optional<InfoEntity> existing = infoRepository.findByDeviceId(infoEntity.getDeviceId());

        if(existing.isPresent()) {
            log.info("Update device info by {}", infoEntity.getDeviceId());
            InfoEntity existingInfo = existing.get();
            existingInfo.setDeviceId(infoEntity.getDeviceId());
            existingInfo.setProvince(infoEntity.getProvince());
            existingInfo.setCity(infoEntity.getCity());
            existingInfo.setDistrict(infoEntity.getDistrict());
            existingInfo.setLatitude(infoEntity.getLatitude());
            existingInfo.setLongitude(infoEntity.getLongitude());
            existingInfo.setTemp(infoEntity.getTemp());
            existingInfo.setWeatherInfo(infoEntity.getWeatherInfo());
            existingInfo.setAqi(infoEntity.getAqi());
            existingInfo.setPm25(infoEntity.getPm25());
            existingInfo.setPm10(infoEntity.getPm10());

            infoRepository.save(existingInfo);
        } else {
            log.info("Save device info by {}", infoEntity.getDeviceId());
            infoRepository.save(infoEntity);
        }

        return ResponseEntity.ok(InfoResponse.success());
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<InfoEntity> getInfo(@PathVariable String deviceId) {
        log.info("Get device info by {}", deviceId);
        return infoRepository.findByDeviceId(deviceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
