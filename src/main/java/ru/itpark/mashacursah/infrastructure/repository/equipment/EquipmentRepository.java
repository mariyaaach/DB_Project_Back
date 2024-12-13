package ru.itpark.mashacursah.infrastructure.repository.equipment;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.mashacursah.entity.equipment.Equipment;

import java.util.List;
import java.util.Map;

@Repository
public class EquipmentRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public EquipmentRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Equipment> findAll() {
        String sql = "SELECT * FROM equipment";
        return jdbcTemplate.query(sql, new EquipmentRowMapper());
    }

    public Equipment findById(Long equipmentId) {
        String sql = "SELECT * FROM equipment WHERE equipment_id = :equipmentId";
        return jdbcTemplate.queryForObject(sql, Map.of("equipmentId", equipmentId), new EquipmentRowMapper());
    }

    public void save(Equipment equipment) {
        String sql = "INSERT INTO equipment (name, description, location, availability_status) " +
                     "VALUES (:name, :description, :location, :availabilityStatus)";
        jdbcTemplate.update(sql, Map.of(
                "name", equipment.getName(),
                "description", equipment.getDescription(),
                "location", equipment.getLocation(),
                "availabilityStatus", equipment.getAvailabilityStatus()
        ));
    }

    public void update(Equipment equipment) {
        String sql = "UPDATE equipment SET name = :name, description = :description, " +
                     "location = :location, availability_status = :availabilityStatus WHERE equipment_id = :equipmentId";
        jdbcTemplate.update(sql, Map.of(
                "equipmentId", equipment.getEquipmentId(),
                "name", equipment.getName(),
                "description", equipment.getDescription(),
                "location", equipment.getLocation(),
                "availabilityStatus", equipment.getAvailabilityStatus()
        ));
    }

    public void delete(Long equipmentId) {
        String sql = "DELETE FROM equipment WHERE equipment_id = :equipmentId";
        jdbcTemplate.update(sql, Map.of("equipmentId", equipmentId));
    }
}