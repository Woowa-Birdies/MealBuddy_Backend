package com.woowa.profile.controller;

import com.woowa.profile.domain.dto.ProfileDto;
import com.woowa.profile.service.ProfileService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/api/profile/search/{userId}")
    public ResponseEntity<ProfileDto> getProfileByUserId(@PathVariable Long userId) {
        try {
            ProfileDto userProfile = profileService.findProfileByUserId(userId);
            return ResponseEntity.ok(userProfile);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
