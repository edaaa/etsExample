package com.example.demo.controller;

import com.example.demo.exception.NotFoundException;
import com.example.demo.fileProcess.dto.FileDeleteDto;
import com.example.demo.fileProcess.dto.FileDto;
import com.example.demo.fileProcess.dto.FileUploadDto;
import com.example.demo.services.FileServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Api
public class FileController {

    @Autowired
    FileServices fileServices;


    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("dosya yükleme işlemi gerçekleştirilir")
    public ResponseEntity<FileUploadDto> singleFileUpload(@RequestParam("file") MultipartFile[] files) {
        return new ResponseEntity<>( fileServices.save(files), HttpStatus.OK);

    }

    @GetMapping(value="/files" )
    @ApiOperation("dosyaları listeleme işlemi gerçekleştirilir")
    public ResponseEntity<List<FileDto>>  listAllFiles() {

        return new ResponseEntity<List<FileDto>>( fileServices.listAll(), HttpStatus.OK);
    }

    @PutMapping(value = "/file/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("dosya güncelleme işlemi gerçekleştirilir")
    public ResponseEntity<FileDto> updateFile(@PathVariable(value = "id") Long id,
                                            @RequestBody FileDto fileDto) throws NotFoundException {
        return new ResponseEntity<>(fileServices.update(id, fileDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/file/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("dosya silme işlemi gerçekleştirilir")
    public ResponseEntity<FileDeleteDto> deleteFile(@PathVariable(value = "id") Long id) throws NotFoundException {

        return new ResponseEntity<>( fileServices.delete(id), HttpStatus.OK);
    }

    @GetMapping(value = "/file/{id}/download")
    @ApiOperation("dosya indirme işlemi gerçekleştirilir")
    public ResponseEntity<ResponseEntity<ByteArrayResource>> downloadFile(@PathVariable("id") Long id) throws NotFoundException {
        return new ResponseEntity<>(fileServices.download(id), HttpStatus.OK);

    }

}
