package com.javatechie.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PdfServiceInterf {
    String extractText(MultipartFile pdfFile) throws IOException;
}
