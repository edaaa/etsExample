package com.example.demo.services;

import com.example.demo.exception.NotFoundException;
import com.example.demo.fileProcess.FileValidator;
import com.example.demo.fileProcess.dto.FileDeleteDto;
import com.example.demo.fileProcess.dto.FileDto;
import com.example.demo.fileProcess.dto.FileUploadDto;
import com.example.demo.fileProcess.entity.FileEntity;
import com.example.demo.repository.FileRepository;
import com.example.demo.util.Constant;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileServicesImpl implements FileServices {

    private final FileRepository repository;
    private final MessageSource messageSource;

    private final FileValidator fileValidator;

    @Override
    public FileUploadDto save(MultipartFile[] files) throws RuntimeException {


        for (MultipartFile file : files) {
            byte[] bytes = new byte[0];
            try {
                bytes = file.getBytes();

                String fileName = file.getOriginalFilename();
                String[] fileNameArray = fileName.split("\\.");

                if (!fileValidator.isContentTypeSupported(FilenameUtils.getExtension(fileName))) {
                    return new FileUploadDto(messageSource.getMessage("file.upload.not.supported.contentType", null, Locale.ENGLISH));
                } else if (!fileValidator.isContentLengthSupported(file.getSize())) {
                    return new FileUploadDto(messageSource.getMessage("file.upload.not.supported.contentSize", null, Locale.ENGLISH));
                }
                Path path = Paths.get(Constant.getBaseUrl() + file.getOriginalFilename());

                Files.write(path, bytes);
                FileEntity entity = new FileEntity();
                entity.setName(fileName);
                entity.setPath(String.valueOf(path));
                entity.setSize(file.getSize());
                repository.save(entity);
                return new FileUploadDto(messageSource.getMessage("file.upload.success", null, Locale.ENGLISH));


            } catch (IOException e) {

                e.printStackTrace();
                return new FileUploadDto(messageSource.getMessage("file.upload.fail", null, Locale.ENGLISH));

            }
        }
        return new FileUploadDto(messageSource.getMessage("file.upload.success", null, Locale.ENGLISH));
    }

    @Override
    public List<FileDto> listAll() {
        List<FileDto> fileList = new ArrayList<>();
        repository.findAll().forEach(file -> {
            fileList.add(new FileDto(file.getPath(), file.getName(), file.getSize()));
        });
        return fileList;
    }


    @Override
    public FileDto update(Long id, FileDto fileDto) throws NotFoundException {
        if (repository.findById((long) id).isPresent()) {
            FileEntity existingFile = repository.findById((long) id).get();
            existingFile.setName(fileDto.getName());
            existingFile.setPath(fileDto.getPath());
            existingFile.setSize(fileDto.getSize());

            FileEntity updatedFileEntity = repository.save(existingFile);
            return new FileDto(updatedFileEntity.getName(), updatedFileEntity.getPath(), updatedFileEntity.getSize());
        } else {
            throw new NotFoundException();
        }
    }


    @Override
    public FileDeleteDto delete(Long id) throws NotFoundException {
        if (repository.existsById(id)) {
            delete(id);
            return new FileDeleteDto(messageSource.getMessage("file.delete.success", null, Locale.ENGLISH));
        } else {
            return new FileDeleteDto(messageSource.getMessage("file.delete.fail", null, Locale.ENGLISH));

        }

    }

    @Override
    public ResponseEntity<ByteArrayResource> download(Long id) throws NotFoundException {
        Optional<FileEntity> fileData = repository.findById(id);
        if (fileData.isEmpty()) {
            throw new NotFoundException();
        }
        Path path = Paths.get(fileData.get().getPath());
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayResource resource = new ByteArrayResource(data);

        ResponseEntity<ByteArrayResource> body = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + path.getFileName().toString())
                .contentType(MediaType.TEXT_PLAIN).contentLength(data.length)
                .body(resource);
        return body;
    }
}



