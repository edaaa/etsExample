package com.example.demo.fileProcess;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Component
public class FileValidator {

   private static final  List<String> supportedContentTypes = Arrays.asList("png", "jpeg", "jpg", "docx", "pdf", "xlsx");
   private static final  int supportedFileSize = 5*1024*1024;
    private final MessageSource messageSource;


    public Boolean isContentTypeSupported(String contentType) {

        return supportedContentTypes.contains(contentType);
    }

    public Boolean isContentLengthSupported(long size) {
        return  size <=supportedFileSize;
    }
}
