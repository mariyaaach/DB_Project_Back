package ru.itpark.mashacursah.entity.equipment;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("equipment_reservations")
public class EquipmentReservation {

    @Id
    private Long reservationId;

    private Long equipmentId;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}