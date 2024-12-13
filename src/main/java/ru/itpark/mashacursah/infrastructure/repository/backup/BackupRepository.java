package ru.itpark.mashacursah.infrastructure.repository.backup;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.mashacursah.entity.backup.Backup;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BackupRepository {

    private final JdbcTemplate jdbcTemplate;

    public BackupRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveBackup(String fileName) {
        String sql = "INSERT INTO backups (file_name) VALUES (?)";
        jdbcTemplate.update(sql, fileName);
    }

    public List<Backup> findAll() {
        String sql = "SELECT * FROM backups";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Backup(
                rs.getString("file_name"),
                rs.getTimestamp("created_at").toLocalDateTime()
        ));
    }
}