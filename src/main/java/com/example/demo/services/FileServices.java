package com.example.demo.services;

import com.example.demo.exception.NotFoundException;
import com.example.demo.fileProcess.dto.FileDeleteDto;
import com.example.demo.fileProcess.dto.FileDto;
import com.example.demo.fileProcess.dto.FileUploadDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface FileServices {

    public FileUploadDto save(MultipartFile[] files);
    public List<FileDto> listAll();
    public FileDto update(Long id, FileDto fileDto) throws NotFoundException;
    public FileDeleteDto delete(Long id) throws NotFoundException;
    public ResponseEntity<ByteArrayResource> download(Long id) throws NotFoundException;
}
