package com.woowa.profile.controller;

import com.woowa.profile.service.ProfileService;
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

    @GetMapping("/profile/search/{userId}")
    public ResponseEntity<?> getProfileByUserId(@PathVariable Long userId) {
        try {
            return new ResponseEntity<>("불러오기 성공 : " + profileService.findProfileByUserId(userId), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("불러오기 실패 : " + userId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}