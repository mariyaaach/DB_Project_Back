package ru.itpark.mashacursah.controllers.http.equipment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itpark.mashacursah.entity.equipment.Equipment;
import ru.itpark.mashacursah.infrastructure.aspects.Log;
import ru.itpark.mashacursah.service.equipment.EquipmentService;

import java.util.List;

@RestController
@Log
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public List<Equipment> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id) {
        Equipment equipment = equipmentService.getEquipmentById(id);
        return equipment != null ? ResponseEntity.ok(equipment) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> createEquipment(@RequestBody Equipment equipment) {
        equipmentService.createEquipment(equipment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEquipment(@PathVariable Long id, @RequestBody Equipment equipment) {
        equipment.setEquipmentId(id);
        equipmentService.updateEquipment(equipment);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }
}