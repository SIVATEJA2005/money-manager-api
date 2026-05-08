package com.project.moneymanager.service;
//import com.project.moneymanager.dto.ExpensesDto;
//import com.project.moneymanager.entity.ProfileEntity;
//import com.project.moneymanager.repository.ProfileRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//@Service
//@RequiredArgsConstructor
//public class EmailServices
//{
//
//    private final JavaMailSender mailSender;
//    private final ProfileRepository  profileRepository;
//    @Value("${spring.mail.properties.mail.smtp.from}")
//    private String fromEmail;
//
//    public void sendEmail(String to,String subject,String body){
//        try
//        {
//            SimpleMailMessage message=new SimpleMailMessage();
//            message.setFrom(fromEmail);
//            message.setTo(to);
//            message.setSubject(subject);
//            message.setText(body);
//            mailSender.send(message);
//        }catch(Exception e)
//        {
//            throw  new RuntimeException(e.getMessage());
//        }
//    }
//    public boolean activate(String activationToken)
//    {
//        ProfileEntity  profile=profileRepository.findByActivationToken(activationToken)
//                .orElseThrow(()->new RuntimeException("InvalidToken"));
//        if(profile.getIsActive())
//        {
//            return false;
//        }
//        profile.setIsActive(true);
//        profile.setActivationToken(null);
//        profile.setUpdatedAt(LocalDateTime.now());
//        profileRepository.save(profile);
//        return true;
//    }
//
//}


import com.project.moneymanager.entity.ExpensesEntity;
import com.project.moneymanager.entity.ProfileEntity;
import com.project.moneymanager.repository.ExpenseRepository;
import com.project.moneymanager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServices
{
    @Value("${BREVO_API_KEY}")
    private String apiKey;
    private final ProfileRepository profileRepository;
    @Value("${BACKEND_URL}")
    private String backendUrl;
    @Value("${FROM_EMAIL}")
    private String fromEmail;

    private final ExpenseRepository expenseRepository;
    private final String SEND_URL = "https://api.brevo.com/v3/smtp/email";

//    private final ProfileServices profileServices;
    public void sendEmail(String toEmail, String subject, String htmlContent) {

        System.out.println(backendUrl+"-"+apiKey);
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("name", "YourAppName", "email", fromEmail));
        body.put("to", List.of(Map.of("email", toEmail))); // ✅ fixed here
        body.put("subject", subject);
        body.put("htmlContent", htmlContent);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(SEND_URL, request, String.class);

        // Optional: log response for debugging
        System.out.println("Brevo response: " + response.getStatusCode() + " | " + response.getBody());

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send email: " + response.getBody());
        }
    }


    public boolean activate(String activationToken)
    {
        ProfileEntity profile=profileRepository.findByActivationToken(activationToken)
                .orElseThrow(()->new RuntimeException("InvalidToken"));
        if(profile.getIsActive())
        {
            return false;
        }
        profile.setIsActive(true);
        profile.setActivationToken(null);
        profile.setUpdatedAt(LocalDateTime.now());
        profileRepository.save(profile);
        return true;
    }

    public void sendIncomeExcel(String toEmail, byte[] excelBytes) {
        RestTemplate restTemplate = new RestTemplate();

        String base64Excel = java.util.Base64.getEncoder().encodeToString(excelBytes);

        Map<String, Object> attachment = new HashMap<>();
        attachment.put("content", base64Excel);
        attachment.put("name", "income_details.xlsx");

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("name", "Money Manager", "email", fromEmail));
        body.put("to", List.of(Map.of("email", toEmail)));
        body.put("subject", "Your Income Details - Money Manager");
        body.put("htmlContent", "<p>Hi,</p><p>Please find attached your complete income details.</p><p>Regards,<br>Money Manager</p>");
        body.put("attachment", List.of(attachment));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(SEND_URL, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send email: " + response.getBody());
        }
    }


    public void sendExpenseExcel(String toEmail, byte[] excelBytes) {
        RestTemplate restTemplate = new RestTemplate();

        String base64Excel = java.util.Base64.getEncoder().encodeToString(excelBytes);

        Map<String, Object> attachment = new HashMap<>();
        attachment.put("content", base64Excel);
        attachment.put("name", "expense_details.xlsx");

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("name", "Money Manager", "email", fromEmail));
        body.put("to", List.of(Map.of("email", toEmail)));
        body.put("subject", "Your Expense Details - Money Manager");
        body.put("htmlContent", "<p>Hi,</p><p>Please find attached your complete expense details.</p><p>Regards,<br>Money Manager</p>");
        body.put("attachment", List.of(attachment));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(SEND_URL, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send email: " + response.getBody());
        }
    }

}



