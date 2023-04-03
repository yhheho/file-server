package com.woven.file.server.controller;

import com.woven.file.server.services.StorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    private final StorageService storageService;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(value = "/{name}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadFile(@PathVariable String name,
                                                   @RequestParam("file") MultipartFile file,
                                                   @RequestParam(value = "replace", defaultValue = "false") boolean replace) throws Exception {
        storageService.store(file, name, replace);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{name}")
    public ResponseEntity<Object> deleteFile(@PathVariable String name) throws Exception {
        storageService.delete(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<String>> listFileNames() {
        List<String> fileNames = storageService.listFileNames();
        return ResponseEntity.ok().body(fileNames);
    }
}
