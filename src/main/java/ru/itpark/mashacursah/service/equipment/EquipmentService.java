package ru.itpark.mashacursah.service.equipment;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.entity.equipment.Equipment;
import ru.itpark.mashacursah.infrastructure.repository.equipment.EquipmentRepository;
import ru.itpark.mashacursah.service.redis.RedisService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final RedisService redisService;

    private static final String EQUIPMENT_CACHE_KEY = "equipment:all";

    public List<Equipment> getAllEquipment() {
        return redisService.getCachedList(
                EQUIPMENT_CACHE_KEY,
                Equipment.class,
                equipmentRepository::findAll
        );
    }

    public Equipment getEquipmentById(Long equipmentId) {
        return equipmentRepository.findById(equipmentId);
    }

    public void createEquipment(Equipment equipment) {
        equipmentRepository.save(equipment);
        redisService.clearCache(EQUIPMENT_CACHE_KEY);
    }

    public void updateEquipment(Equipment equipment) {
        equipmentRepository.update(equipment);
        redisService.clearCache(EQUIPMENT_CACHE_KEY);
    }

    public void deleteEquipment(Long equipmentId) {
        equipmentRepository.delete(equipmentId);
        redisService.clearCache(EQUIPMENT_CACHE_KEY);
    }
}