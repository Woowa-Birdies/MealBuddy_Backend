package com.woowa.profile.service;

import com.woowa.profile.domain.dto.ProfileDto;
import com.woowa.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional
    public List<ProfileDto> findProfileByUserId(Long user_Id) {
        List<Object[]> result = profileRepository.findProfileByUserId(user_Id);
        return result.stream()
            .map(objects -> new ProfileDto((Long) objects[0], (Long) objects[1]))
            .collect(Collectors.toList());
    }
}
