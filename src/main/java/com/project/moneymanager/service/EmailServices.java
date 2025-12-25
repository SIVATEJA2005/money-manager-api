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


import com.project.moneymanager.entity.ProfileEntity;
import com.project.moneymanager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
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
    private final String SEND_URL = "https://api.brevo.com/v3/smtp/email";
    public void sendEmail(String toEmail, String subject, String htmlContent) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("name", "YourAppName", "email", fromEmail));
        body.put("to", new Map[]{ Map.of("email", toEmail) });
        body.put("subject", subject);
        body.put("htmlContent", htmlContent);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(SEND_URL, request, String.class);

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

}



