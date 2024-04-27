package com.woowa.profile.service;

import com.woowa.gather.domain.Post;
import com.woowa.gather.repository.PostRepository;
import com.woowa.profile.domain.dto.ParticipationDto;
import com.woowa.profile.domain.dto.ProfileDto;
import com.woowa.profile.domain.dto.RecruitmentDto;
import com.woowa.profile.repository.ProfileRepository;
import com.woowa.review.repository.ReviewRepository;
import com.woowa.user.domain.User;
import com.woowa.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProfileService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ProfileRepository profileRepository;
    private final PostRepository postRepository;

    @Autowired
    public ProfileService(UserRepository userRepository, ReviewRepository reviewRepository, ProfileRepository profileRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.profileRepository = profileRepository;
        this.postRepository = postRepository;
    }

    /**
         attribute[0] : userId
         attribute[1] : 약속 체크 항목(매너)
         attribute[2] : 약속 체크 항목(비매너)
         attribute[3] : 사회성 체크 항목(매너)
         attribute[4] : 사회성 체크 항목(비매너)
         attribute[5] : 매너 체크 항목(매너)
         attribute[6] : 매너 체크 항목(비매너)
         attribute[7] : 응답 체크 항목(매너)
         attribute[8] : 응답 체크 항목(비매너)
     */
    @Transactional
    public ProfileDto findProfileByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            log.info("userId = {}",userId,ProfileService.class);
            Pageable pageable = PageRequest.of(0, 5);

            // 다른 사용자의 모집글에 참여한 리스트
            List<Long> participationIds = profileRepository.findMyParticipationInfoByUserId(userId,pageable);
            List<ParticipationDto> participationInfos = new ArrayList<>();
            for(Long postId : participationIds) {
                log.info("postID! = {}",postId,ProfileService.class);
                Post post = postRepository.findById(postId).orElse(null);
                if(post != null) {
                    ParticipationDto participationDto = new ParticipationDto();
                    participationDto.setMeetAt(post.getMeetAt());
                    participationDto.setContents(post.getContents());
                    participationInfos.add(participationDto);
                }
            }

            // 내가 만든 모집글 리스트
            List<Post> myPosts = postRepository.findMyRecruitmentInfoByUserId(userId, pageable);
            List<RecruitmentDto> recruitmentInfos = new ArrayList<>();
            for(Post myPost : myPosts) {
                if(myPost != null) {
                    RecruitmentDto recruitmentDto = new RecruitmentDto();
                    recruitmentDto.setMeetAt(myPost.getMeetAt());
                    recruitmentDto.setContents(myPost.getContents());
                    recruitmentInfos.add(recruitmentDto);
                }
            }

            // 유저 평가 항목
            List<Object[]> reviewAttributesByUserId = reviewRepository.countReviewAttributesByUserId(userId);
            int goodPunctuality = 0 , badPunctuality = 0, goodSociability = 0, badSociability = 0;
            int goodManner = 0, badManner = 0, goodReply = 0, badReply = 0;

            for(Object[] attribute : reviewAttributesByUserId) {
                goodPunctuality = attribute[1] != null ? Integer.parseInt(String.valueOf(attribute[1])) : 0;
                badPunctuality = attribute[2] != null ? Integer.parseInt(String.valueOf(attribute[2])) : 0;
                goodSociability = attribute[3] != null ? Integer.parseInt(String.valueOf(attribute[3])) : 0;
                badSociability = attribute[4] != null ? Integer.parseInt(String.valueOf(attribute[4])) : 0;
                goodManner = attribute[5] != null ? Integer.parseInt(String.valueOf(attribute[5])) : 0;
                badManner = attribute[6] != null ? Integer.parseInt(String.valueOf(attribute[6])) : 0;
                goodReply = attribute[7] != null ? Integer.parseInt(String.valueOf(attribute[7])) : 0;
                badReply = attribute[8] != null ? Integer.parseInt(String.valueOf(attribute[8])) : 0;
            }
            ProfileDto dto = ProfileDto.fromUser(
                    user,
                    goodPunctuality,badPunctuality,goodSociability,badSociability,goodManner,badManner,goodReply,badReply,
                    participationInfos,recruitmentInfos);
            return dto;
        }
        else {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
    }
}
