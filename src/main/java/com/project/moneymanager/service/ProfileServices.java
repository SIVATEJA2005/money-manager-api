package com.project.moneymanager.service;
import com.project.moneymanager.dto.AuthDto;
import com.project.moneymanager.dto.ProfileDto;
import com.project.moneymanager.entity.ProfileEntity;
import com.project.moneymanager.repository.ProfileRepository;
import com.project.moneymanager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServices {

    private final ProfileRepository profileRepository;

    private final EmailServices emailServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${activation.link.url}")
    private String activationLink;

    public ProfileDto toProfileDto(ProfileEntity profile)
    {

        return ProfileDto
                .builder()
                .id(profile.getId())
                .email(profile.getEmail())
                .name(profile.getName())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();

    }

    public ProfileEntity toProfileEntity(ProfileDto profileDto)
    {

        return ProfileEntity.builder()
                .name(profileDto.getName())
                .email(profileDto.getEmail())
                .password(passwordEncoder.encode(profileDto.getPassword()))
                .imageUrl(profileDto.getImageUrl())
                .activationToken(UUID.randomUUID().toString())
                .isActive(false)
                .build();
    }

    public ProfileDto register(ProfileDto profileDto)
    {

            ProfileEntity profileEntity=toProfileEntity(profileDto);
            ProfileEntity savedProfileEntity=profileRepository.save(profileEntity);
            String activateLink=activationLink+"="+profileEntity.getActivationToken();
            String subject="Activivate you money manager account";
            String body="click on the following link to activate your account "+activateLink;
            emailServices.sendEmail(profileEntity.getEmail(),subject,body);
            return toProfileDto(savedProfileEntity);

    }

    public boolean isActive(String email)
    {
        ProfileEntity profile=profileRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("user not found"));
        if(!profile.getIsActive())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public ProfileDto getProfile(String email)
    {
        try{
            ProfileEntity profileEntity=profileRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("user not found"));
            return toProfileDto(profileEntity);
        }catch(Exception e){
            throw new RuntimeException("Invalid email");
        }
    }


    public Map<String, Object> authenticateAndGenerateAuthToekn(AuthDto authDto)
    {
            try
            {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(),authDto.getPassword()));
                return Map.of(
                        "token",jwtUtil.generateToken(authDto.getEmail()),
                        "user",getProfile(authDto.getEmail())
                );

            }catch(Exception e)
            {
                throw new RuntimeException("email or password not found");
            }
    }


    public ProfileEntity getCurrentProfile()
    {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        if(auth==null)
        {
            throw null;
        }
        String email=auth.getName();
        return profileRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("user not found "+email));
    }

}
