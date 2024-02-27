package com.javatechie.services;


import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.javatechie.dto.ChatGPTRequest;
import com.javatechie.dto.ChatGptResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PdfService implements PdfServiceInterf{

    @Override
    public String extractText(MultipartFile pdfFile) throws IOException {
        PdfReader pdfReader = new PdfReader(pdfFile.getInputStream());
        int numberOfPages = pdfReader.getNumberOfPages();

        StringBuilder text = new StringBuilder();

        for (int i = 1; i <= numberOfPages; i++) {
            text.append(PdfTextExtractor.getTextFromPage(pdfReader, i));
        }

        return text.toString();

    }


    @Value("${openai.model}")
    private String model;

    @Value(("${openai.api.url}"))
    private String apiURL;

    @Autowired
    private RestTemplate template;


    public String chat(String extractedText){
       String prompt = "Gere um resumo jurídico de fácil entendimento e providências a serem tomadas" +
                "do seguinte texto: " + extractedText;
        ChatGPTRequest request=new ChatGPTRequest(model, prompt);
        ChatGptResponse chatGptResponse = template.postForObject(apiURL, request, ChatGptResponse.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }
}
