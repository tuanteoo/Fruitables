package com.backend.Controller;

import com.backend.Configuration.Translator;
import com.backend.DTO.Response.ResponseData;
import com.backend.DTO.Response.ResponseError;
import com.backend.Service.ImgBBService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@Slf4j
@Tag(name = "File Controller")
@RequiredArgsConstructor
public class FileUploadController {
    private final ImgBBService imgBBService;
    private static final String ERROR_MESSAGE = "errorMessage={}";

    @PostMapping("/upload")
    public ResponseData<?> upload(@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        try {
            String imageUrl = imgBBService.uploadImage(file);
            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("user.add.success"), imageUrl);
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add user fail");
        }
    }
}
