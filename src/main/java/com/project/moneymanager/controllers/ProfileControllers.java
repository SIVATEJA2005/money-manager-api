package com.project.moneymanager.controllers;
import com.project.moneymanager.dto.AuthDto;
import com.project.moneymanager.dto.ProfileDto;
import com.project.moneymanager.service.EmailServices;
import com.project.moneymanager.service.ProfileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.Map;
@RestController
@RequestMapping("/api/v1.0")
public class ProfileControllers
{
    @Autowired
    private EmailServices emailServices;
    @Autowired
    private ProfileServices profileServices;
    @PostMapping("/register")
    public ResponseEntity<ProfileDto> register(@RequestBody ProfileDto profileDto){
        System.out.println("in register");
        ProfileDto savedProfileDto=profileServices.register(profileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfileDto);
    }
    @GetMapping("/activate")
    public ResponseEntity<String> activate(@RequestParam String token){
        boolean isActivated= emailServices.activate(token);
        if(isActivated){
            return ResponseEntity.ok("Profile activated succesfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("invalid token or ");
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDto authDto){
        try
        {
            boolean isActive = profileServices.isActive(authDto.getEmail());
            System.out.println(isActive);
            if (!isActive) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "message", "account is not active active your accoutn first"
                ));
            }
            Map<String, Object> response = profileServices.authenticateAndGenerateAuthToekn(authDto);
            return ResponseEntity.ok(response);
        }catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message",e.getMessage()
            ));
        }
    }
    @GetMapping("/test")
    public String testJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "Authenticated user: " + auth.getName();
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> getPublicProfile() {
        ProfileDto profileDTO = profileServices.getPublicProfile(null);
        return ResponseEntity.ok(profileDTO);
    }


}
