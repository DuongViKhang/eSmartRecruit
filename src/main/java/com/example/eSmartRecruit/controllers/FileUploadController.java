package com.example.eSmartRecruit.controllers;

import com.example.eSmartRecruit.services.IStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eSmartRecruit/fileUpload")
public class FileUploadController {

    private IStorageService storageService;
    @PostMapping("")
    public ResponseEntity<List<String>> uploadFile(@RequestParam("file")MultipartFile file){
        try {
            String generatedFileName = storageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    List.of("ok","upload successfully",generatedFileName)
            );
        }catch (Exception e){
            return new ResponseEntity<List<String>>(List.of("ok", e.getMessage(),""),HttpStatus.NOT_IMPLEMENTED);
        }
    }
}
