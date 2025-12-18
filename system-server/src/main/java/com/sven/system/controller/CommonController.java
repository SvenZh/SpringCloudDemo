package com.sven.system.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonController {

    @RequestMapping(value = "/fileDownload")
    public ResponseEntity<Resource> fileDownloadDemo() throws Exception {
        String downloadFileName = "test.xlsx";
        String filePath = "test.xlsx";

        Resource resource = new ClassPathResource(filePath);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(downloadFileName + ".xlsx", StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .body(resource);
    }
}
