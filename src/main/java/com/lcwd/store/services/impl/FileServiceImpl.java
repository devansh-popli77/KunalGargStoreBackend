package com.lcwd.store.services.impl;

import com.lcwd.store.exceptions.BadApiRequestException;
import com.lcwd.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {
        String orginalFileName = file.getOriginalFilename();
        logger.info("Filename : {}", orginalFileName);
        String filename = UUID.randomUUID().toString();
        String extension = orginalFileName.substring(orginalFileName.lastIndexOf("."));
        String fileNameWithExtension = filename + extension;
        String fullPathWithFileName = path + fileNameWithExtension;
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
            File folder = new File(path);
            if (!folder.exists()) {
                // create the folder
                folder.mkdirs();
            }
            //upload file
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
        } else {
            throw new BadApiRequestException("File with this " + extension + "not allowed");
        }
        return fileNameWithExtension;
    }

    @Override
    public InputStream getResouce(String path, String name) throws FileNotFoundException {
        String fullPath = path + File.separator + name;
        InputStream inputStream = new FileInputStream(fullPath);

        return inputStream;
    }
}
