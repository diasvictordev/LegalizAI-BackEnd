package com.javatechie.controller;

import com.javatechie.services.PdfService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
public class PDFController {


    private final PdfService pdfService;

    public PDFController(PdfService pdfService) {
        this.pdfService = pdfService;
    }


    @PostMapping("/upload")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.getContentType().equalsIgnoreCase("application/pdf")) {
                return ResponseEntity.badRequest().body("O arquivo deve ser um PDF.");
            }

            String texto = pdfService.extractText(file);

            return ResponseEntity.ok(texto);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor.");
        }
    }

    @PostMapping("/upload/gpt")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<String> gptFile(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.getContentType().equalsIgnoreCase("application/pdf")) {
                return ResponseEntity.badRequest().body("O arquivo deve ser um PDF.");
            }

            String texto = pdfService.extractText(file);
            String extractedFile = pdfService.chat(texto);
            return ResponseEntity.ok().body(extractedFile);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor.");
        }
    }



}
