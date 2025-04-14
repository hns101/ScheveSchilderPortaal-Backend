package nl.scheveschilder.scheveschilderportaal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class ArtworkPhotoService {

    private final Path fileStoragePath;

    public ArtworkPhotoService(@Value("${my.upload_location}") String uploadDir) throws IOException {
        this.fileStoragePath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(fileStoragePath);
    }

    public String storePhoto(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path targetPath = this.fileStoragePath.resolve(fileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public Resource getPhoto(String fileName) throws MalformedURLException {
        Path path = this.fileStoragePath.resolve(fileName);
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Foto niet gevonden of onleesbaar.");
        }
        return resource;
    }

    public void deletePhoto(String filename) {
        try {
            Path path = this.fileStoragePath.resolve(filename); // ✅ FIXED
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Kon bestand niet verwijderen: " + filename, e);
        }
    }
}
