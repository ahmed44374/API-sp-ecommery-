package com.example.specommercy.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        String currImageName = image.getOriginalFilename();
        String updatedImageName = UUID.randomUUID()
                .toString()
                .concat(currImageName.substring(currImageName.lastIndexOf('.')));

        String imagePath = path + File.separator + updatedImageName;
        File directory = new File(path);
        if(!directory.exists())
            directory.mkdir();
        Files.copy(image.getInputStream(), Paths.get(imagePath));
        return updatedImageName;
    }
}
