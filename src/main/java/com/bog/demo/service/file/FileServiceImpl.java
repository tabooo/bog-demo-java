package com.bog.demo.service.file;

import com.bog.demo.domain.file.File;
import com.bog.demo.exception.FileStorageException;
import com.bog.demo.repository.file.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Transactional(rollbackFor = Throwable.class, readOnly = true)
public class FileServiceImpl implements FileService {

    private static final int FILE_MAX_COUNT = 10000;

    @Value("${app.demo.data-directory-path}")
    private String dataDirectory;

    private FileRepository fileRepository;

    private Path targetLocation;

    @Override
    public File getFile(Integer fileId) {
        Optional<File> fileOption = fileRepository.findById(fileId);
        fileOption.orElseThrow(() -> new IllegalArgumentException("Not found: " + fileId));

        return fileOption.get();
    }

    @Override
    @Transactional
    public void removeFile(Integer fileId) {

        File file = getFile(fileId);
        file.setState(1);
        fileRepository.save(file);
    }

    @Override
    public Resource getResource(Integer fileId) throws FileNotFoundException {
        File file = getFile(fileId);

        if (Objects.nonNull(file)) {
            try {
                Path filePath = Paths.get(file.getPath());
                Resource resource = new UrlResource(filePath.toUri());

                if (resource.exists()) {
                    return resource;
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        throw new FileNotFoundException("File not found");
    }

    @Override
    @Transactional
    public File store(MultipartFile file, Integer fileTypeId) {

        if (file != null && file.getSize() != 0) {
            if (Objects.isNull(file.getOriginalFilename())) {
                throw new FileStorageException("empty file name");
            }

            String name = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String ext = getFileExtension(name);
            String mimeType = file.getContentType();

            if (isValidFile(name)) {
                throw new FileStorageException("invalid file name");
            }

            File fileObject = addFile(name, fileTypeId, ext, mimeType);
            String dirName = getDirNumber(fileObject.getId()).toString();
            createDirectory(dirName);

            String fileStorageName = fileObject.getId().toString() + ext;
            fileObject.setPath(uploadFile(file, fileStorageName));

            return fileObject;
        } else {
            throw new FileStorageException("empty file");
        }
    }

    private String uploadFile(MultipartFile file, String fileName) {
        String path;

        try {
            Files.copy(file.getInputStream(), targetLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            path = targetLocation.resolve(fileName).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new FileStorageException("could not store the file");
        }

        return path;
    }

    private File addFile(String name, Integer fileTypeId, String ext, String mimeType) {
        File fileObject = new File();

        fileObject.setName(name);
        fileObject.setMimeType(mimeType);
        fileObject.setExt(ext);
        fileObject.setInsertDate(LocalDateTime.now());
        fileObject.setState(1);

        return fileRepository.save(fileObject);
    }

    private boolean isValidFile(String originalFilename) {
        String uploadFileName = StringUtils.cleanPath(originalFilename);

        if (uploadFileName.contains("..")) {
            return false;
        }

        //TODO [I.G] check mime types and ext
        return false;
    }

    private String getFileExtension(String originalFilename) {
        Integer lastIndexOfDot = originalFilename.lastIndexOf('.');

        return originalFilename.substring(lastIndexOfDot, originalFilename.length());
    }

    private void createDirectory(String dirName) {
        try {
            targetLocation = Paths.get(dataDirectory + "/" + dirName).toAbsolutePath().normalize();

            if (!Files.exists(targetLocation)) {
                Files.createDirectory(targetLocation);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new FileStorageException("could not create folder");
        }
    }

    @Override
    public List<File> getFilesByFileIds(Set<Integer> fileIds) {
        return fileRepository.getFileByFileIds(fileIds);
    }

    private Integer getDirNumber(Integer fileId) {
        return fileId / FILE_MAX_COUNT;
    }

    @Autowired
    public void setFileRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
}