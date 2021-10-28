package com.bog.demo.controller;

import com.bog.demo.service.file.FileService;
import com.bog.demo.util.Descriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

@Slf4j
@RestController("fileController")
@RequestMapping("rest/api/file")
public class FileController {

    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<Descriptor> uploadFile(@RequestParam("file") final MultipartFile file,
                                                 @RequestParam("fileTypeId") final Integer fileTypeId) {

        try {
            return new ResponseEntity<>(new Descriptor(true, fileService.store(file, fileTypeId)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Descriptor(false, e.getMessage()), HttpStatus.OK);
        }
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Integer id, HttpServletRequest request,
                                            HttpServletResponse response) {

        try {
            Resource resource = fileService.getResource(id);
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

            String ext = resource.getFilename().substring(resource.getFilename().lastIndexOf('.') + 1);

            if (ext.equalsIgnoreCase("pdf")) {
                response.setContentType("application/pdf; charset=UTF-8");
            } else if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg")
                    || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("gif")) {
                response.setHeader("Content-Type", "image/jpeg");
            } else {
                response.setHeader("Content-Disposition", "attachment; filename=" + resource.getFilename());
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
