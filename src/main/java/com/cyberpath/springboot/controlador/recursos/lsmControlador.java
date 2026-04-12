package com.cyberpath.springboot.controlador.recursos;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.time.Duration;
import java.util.Optional;

@RestController
@RequestMapping("/smartlearn/api/lsm")
@CrossOrigin // opcional; no necesario para peticiones desde apps Android, pero útil en pruebas web
public class lsmControlador {

    @Value("${lsm.basePath:/var/www/lsm}")
    private String basePath;

    private Path assetsDir;
    private Path mappingsDir;

    @PostConstruct
    public void init() throws IOException {
        assetsDir = Paths.get(basePath, "assets");
        mappingsDir = Paths.get(basePath, "mappings");
        Files.createDirectories(assetsDir);
        Files.createDirectories(mappingsDir);
    }

    @GetMapping(value = "/mapping/{lessonId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> getMapping(@PathVariable String lessonId) {
        try {
            Path file = mappingsDir.resolve(lessonId + ".json").normalize();
            if (!Files.exists(file) || !file.startsWith(mappingsDir)) {
                return ResponseEntity.notFound().build();
            }
            Resource resource = new FileSystemResource(file.toFile());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .cacheControl(CacheControl.maxAge(Duration.ofHours(24)))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/assets/{filename:.+}")
    public ResponseEntity<Resource> getAsset(@PathVariable String filename) {
        try {
            Path file = assetsDir.resolve(filename).normalize();
            if (!Files.exists(file) || !file.startsWith(assetsDir)) {
                return ResponseEntity.notFound().build();
            }
            Resource resource = new FileSystemResource(file.toFile());

            // Intentamos detectar tipo MIME
            Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(filename);

            MediaType contentType = mediaType.orElse(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .contentType(contentType)
                    .cacheControl(CacheControl.maxAge(Duration.ofDays(7)))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/uploadAsset", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAsset(@RequestParam("file") MultipartFile file,
                                              @RequestParam(value = "filename", required = false) String filename) {
        try {
            if (file.isEmpty()) return ResponseEntity.badRequest().body("Empty file");
            String original = file.getOriginalFilename();
            String targetName = (filename == null || filename.trim().isEmpty()) ? original : filename;
            if (targetName == null) return ResponseEntity.badRequest().body("Filename required");
            Path target = assetsDir.resolve(targetName).normalize();
            if (!target.startsWith(assetsDir)) return ResponseEntity.badRequest().body("Invalid filename");
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return ResponseEntity.ok("Uploaded: " + targetName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping(value = "/uploadMapping", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadMapping(@RequestParam("lessonId") String lessonId,
                                                @RequestBody String mappingJson) {
        try {
            if (lessonId == null || lessonId.trim().isEmpty()) return ResponseEntity.badRequest().body("lessonId required");
            Path target = mappingsDir.resolve(lessonId + ".json").normalize();
            if (!target.startsWith(mappingsDir)) return ResponseEntity.badRequest().body("Invalid lessonId");
            Files.write(target, mappingJson.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return ResponseEntity.ok("Mapping saved: " + lessonId + ".json");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/assets/{filename:.+}")
    public ResponseEntity<String> deleteAsset(@PathVariable String filename) {
        try {
            Path f = assetsDir.resolve(filename).normalize();
            if (!f.startsWith(assetsDir) || !Files.exists(f)) return ResponseEntity.notFound().build();
            Files.delete(f);
            return ResponseEntity.ok("Deleted: " + filename);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}