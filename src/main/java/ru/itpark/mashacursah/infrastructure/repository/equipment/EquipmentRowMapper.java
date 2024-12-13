package ru.itpark.mashacursah.infrastructure.repository.equipment;

import org.springframework.jdbc.core.RowMapper;
import ru.itpark.mashacursah.entity.equipment.Equipment;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EquipmentRowMapper implements RowMapper<Equipment> {

    @Override
    public Equipment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Equipment equipment = new Equipment();

        equipment.setEquipmentId(rs.getLong("equipment_id"));
        equipment.setName(rs.getString("name"));
        equipment.setDescription(rs.getString("description"));
        equipment.setLocation(rs.getString("location"));
        equipment.setAvailabilityStatus(rs.getString("availability_status"));

        return equipment;
    }
}