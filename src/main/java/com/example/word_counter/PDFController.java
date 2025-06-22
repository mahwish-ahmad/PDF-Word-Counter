package com.example.word_counter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api")
public class PDFController {
    
    @PostMapping("/upload")

    public Map<String, Object> handleFileUpload(@RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {

            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            text = text.trim().replaceAll("\\r", "");

            int wordCount = text.isBlank() ? 0 : text.split("\\s+").length;
            int charCount = text.replaceAll("\\s", "").length();
            int paragraphCount = text.split("\\n\\s*\\n").length;

            response.put("wordCount", wordCount);
            response.put("charCount", charCount);
            response.put("paragraphCount", paragraphCount);
        }

        catch (IOException e) {
            response.put("Error", "Failed to process PDF: " + e.getMessage());
        }

        return response;
    }
}
