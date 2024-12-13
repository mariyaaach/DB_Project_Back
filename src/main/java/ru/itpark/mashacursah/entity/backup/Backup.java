package ru.itpark.mashacursah.entity.backup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Backup {
    private final String fileName;
    private final LocalDateTime createdAt;
}
