package com.fa.ims.service.impl;

import com.fa.ims.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    private final String fileSource =  "./uploads";


    @Override
    public String saveFile(MultipartFile file, String email) {
        try {
            Path folderPath = Paths.get(fileSource).resolve(email);
            Files.createDirectories(folderPath);
            Path filePath = Paths.get(email).resolve(file.getOriginalFilename());
            file.transferTo(folderPath.resolve(file.getOriginalFilename()));
            return filePath.toString();
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource loadFile(String path) {
        try {
            Path absolutePath = Paths.get(fileSource).resolve(path);
            Resource resource = new UrlResource(absolutePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
