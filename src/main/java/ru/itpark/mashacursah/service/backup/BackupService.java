package ru.itpark.mashacursah.service.backup;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.entity.backup.Backup;
import ru.itpark.mashacursah.infrastructure.async.TaskPublisher;
import ru.itpark.mashacursah.infrastructure.configuration.redis.publisher.NotificationPublisher;
import ru.itpark.mashacursah.infrastructure.configuration.redis.publisher.dto.NotificationEvent;
import ru.itpark.mashacursah.infrastructure.repository.backup.BackupRepository;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BackupService {

    @Getter
    @Value("${backup.directory:/path/to/backup/directory}")
    private String backupDir;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${backup.dbName}")
    private String dbName;

    private final TaskPublisher taskPublisher;
    private final BackupRepository backupRepository;
    private final NotificationPublisher notificationPublisher;
    private static final long TIMEOUT_MILLIS = 60 * 1000;
    private static final String POSTGRES_CONTAINER_NAME = "postgres";



    public String createBackup() throws Exception {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        NotificationEvent event = new NotificationEvent();
        event.setType("Бэкап начался!");
        event.setMessage("Успешное начало бэкапа время: " + timestamp);
        notificationPublisher.publishEvent("notifications", event);

        String backupFileName = "backup_" + timestamp + ".dump";
        String backupPath = Paths.get(backupDir, backupFileName).toString();

        Files.createDirectories(Paths.get(backupDir));

        createDump();
        copyBackupFromContainer(backupPath);

        backupRepository.saveBackup(backupFileName);

        NotificationEvent completionEvent = new NotificationEvent();
        completionEvent.setType("Бэкап завершен!");
        completionEvent.setMessage("Проверьте свою локальную папку - все будет там");
        notificationPublisher.publishEvent("notifications", completionEvent);

        return backupFileName;
    }

    public void restoreBackup(String fileName) throws Exception {
        String backupPath = Paths.get(backupDir, fileName).toString();

        copyBackupToContainer(backupPath);
        applyDump();
    }

    private void createDump() throws Exception {
        String command = String.format(
                "docker exec %s pg_dump -U %s -d %s -F c -b -v -f /tmp/backup.dump",
                POSTGRES_CONTAINER_NAME, dbUser, dbName
        );
        taskPublisher.submit(command, TIMEOUT_MILLIS);
    }

    private void copyBackupFromContainer(String backupPath) throws Exception {
        String copyCommand = String.format("docker cp %s:/tmp/backup.dump %s", POSTGRES_CONTAINER_NAME, backupPath);
        taskPublisher.submit(copyCommand, TIMEOUT_MILLIS);
    }

    private void copyBackupToContainer(String backupPath) throws Exception {
        String copyCommand = String.format("docker cp %s %s:/tmp/backup.dump", backupPath, POSTGRES_CONTAINER_NAME);
        taskPublisher.submit(copyCommand, TIMEOUT_MILLIS);
    }

    private void applyDump() throws Exception {
        String restoreCommand = String.format(
                "docker exec -t %s pg_restore -U %s -d %s -v /tmp/backup.dump",
                POSTGRES_CONTAINER_NAME, dbUser, dbName
        );
        taskPublisher.submit(restoreCommand, TIMEOUT_MILLIS);
    }

    public List<Backup> findAll() {
        return backupRepository.findAll();
    }
}