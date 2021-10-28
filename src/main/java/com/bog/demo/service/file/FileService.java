package com.bog.demo.service.file;

import com.bog.demo.domain.file.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

public interface FileService {

    File getFile(Integer fileId);

    void removeFile(Integer fileId);

    Resource getResource(Integer fileId) throws FileNotFoundException;

    File store(MultipartFile file, Integer fileTypeId);

    List<File> getFilesByFileIds(Set<Integer> fileIds);
}