package ru.itpark.mashacursah.entity.equipment;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("equipment")
public class Equipment {

    @Id
    private Long equipmentId;

    private String name;
    private String description;
    private String location;
    private String availabilityStatus;
}