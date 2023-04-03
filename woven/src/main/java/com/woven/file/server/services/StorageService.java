package com.woven.file.server.services;

import com.woven.file.server.constant.Constant;
import com.woven.file.server.exception.DiskSpaceNotEnoughException;
import com.woven.file.server.exception.FileSizeTooBigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class StorageService {

    private final long GB = 1024 * 1024 * 1024;
    private final String BASE_FOLDER =
            System.getProperty("os.name").toLowerCase().contains("windows") ? ".\\" : "./";
    private static final Logger log = LoggerFactory.getLogger(StorageService.class);


    public void store(MultipartFile multipartFile, String name, boolean replace) throws Exception {
        String filePath = BASE_FOLDER + name;
        if (checkIfExists(filePath)) {
            if (!replace) {
                log.error("File name already exists: {}", name);
                throw new FileAlreadyExistsException(Constant.FILE_NAME_ALREADY_EXIST);
            }
        }
        if (multipartFile.getSize() > GB) {
            log.error("File size too large: {}", multipartFile.getSize());
            throw new FileSizeTooBigException(Constant.FILE_SIZE_TOO_LARGE);
        }
        if (!hasEnoughDiskSpace(multipartFile.getSize())) {
            log.error("Disk space is not enough");
            throw new DiskSpaceNotEnoughException(Constant.DISK_SPACE_NOT_ENOUGH);
        }

        try {
            Path path = Paths.get(filePath);
            Files.write(path, multipartFile.getBytes());
        } catch (Exception e) {
            log.error("Failed to store file: ", e);
            throw new RuntimeException("store file failed: ", e);
        }
    }

    public void delete(String name) throws Exception {
        String filePath = BASE_FOLDER + name;
        if (!checkIfExists(filePath)) {
            log.error("File not found: " + name);
            throw new FileNotFoundException(Constant.FILE_NOT_FOUND);
        }
        try {
            Files.delete(Paths.get(filePath));
        } catch (Exception e) {
            log.error("Failed to delete file: ", e);
            throw new RuntimeException("delete file failed: ", e);
        }
    }

    public List<String> listFileNames() {
        try {
            return Files.list(Paths.get(BASE_FOLDER))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to list file names", e);
            throw new RuntimeException("list file name failed");
        }
    }

    private boolean checkIfExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    private boolean hasEnoughDiskSpace(long fileSize) throws IOException {
        Path path = Paths.get(BASE_FOLDER);
        FileStore store = Files.getFileStore(path);
        return fileSize <= store.getUsableSpace();
    }
}
