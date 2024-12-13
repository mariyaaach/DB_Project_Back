package ru.itpark.mashacursah.service.equipment;

import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.entity.equipment.Equipment;
import ru.itpark.mashacursah.infrastructure.repository.equipment.EquipmentRepository;

import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Equipment getEquipmentById(Long equipmentId) {
        return equipmentRepository.findById(equipmentId);
    }

    public void createEquipment(Equipment equipment) {
        equipmentRepository.save(equipment);
    }

    public void updateEquipment(Equipment equipment) {
        equipmentRepository.update(equipment);
    }

    public void deleteEquipment(Long equipmentId) {
        equipmentRepository.delete(equipmentId);
    }
}