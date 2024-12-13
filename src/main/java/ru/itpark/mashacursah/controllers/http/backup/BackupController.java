package ru.itpark.mashacursah.controllers.http.backup;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.itpark.mashacursah.entity.backup.Backup;
import ru.itpark.mashacursah.infrastructure.aspects.Log;
import ru.itpark.mashacursah.service.backup.BackupService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@Log
@RequestMapping("/api/backup")
public class BackupController {

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    // Эндпоинт для создания бэкапа
    @PostMapping
    public ResponseEntity<String> createBackup() {
        try {
            String backupFileName = backupService.createBackup();
            return ResponseEntity.ok(backupFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при создании бэкапа: " + e.getMessage());
        }
    }

    // Эндпоинт для восстановления из бэкапа
    @PostMapping("/restore")
    public ResponseEntity<String> restoreBackup(@RequestParam("fileName") String fileName) {
        try {
            if (!StringUtils.hasText(fileName) || fileName.contains("..") || !fileName.endsWith(".dump")) {
                return ResponseEntity.badRequest().body("Некорректное имя файла.");
            }

            String backupPath = Paths.get(backupService.getBackupDir(), fileName).toString();
            if (!Files.exists(Paths.get(backupPath))) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Файл бэкапа не найден.");
            }

            backupService.restoreBackup(fileName);
            return ResponseEntity.ok("База данных успешно восстановлена из бэкапа: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при восстановлении бэкапа: " + e.getMessage());
        }
    }

    // Эндпоинт для загрузки файла бэкапа
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadBackup(@RequestParam("fileName") String fileName) {
        try {
            if (!StringUtils.hasText(fileName) || fileName.contains("..") || !fileName.endsWith(".dump")) {
                return ResponseEntity.badRequest().body(null);
            }

            String backupPath = Paths.get(backupService.getBackupDir(), fileName).toString();
            if (!Files.exists(Paths.get(backupPath))) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Подготовка файла для скачивания
            FileSystemResource resource = new FileSystemResource(backupPath);

            // Настройка заголовков для корректной загрузки
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/list")
    public List<Backup> findAll() {
        return backupService.findAll();
    }
}