package com.woowa.gather.domain;

import com.woowa.common.domain.BaseEntity;
import com.woowa.gather.domain.dto.PostUpdateDto;
import com.woowa.gather.domain.enums.Age;
import com.woowa.gather.domain.enums.FoodType;
import com.woowa.gather.domain.enums.Gender;
import com.woowa.gather.domain.enums.PostStatus;
import com.woowa.room.domain.Room;
import com.woowa.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    private int participantTotal;
    private int participantCount;

    @Column(columnDefinition = "text")
    private String contents;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @Enumerated(EnumType.STRING)
    private FoodType foodTypeTag;

    @Enumerated(EnumType.STRING)
    private Age ageTag;

    @Enumerated(EnumType.STRING)
    private Gender genderTag;

    private int viewCount;

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Ask> asks = new ArrayList<>();
    @OneToOne(mappedBy = "post")
    private Room room;
    private LocalDateTime meetAt;
    private LocalDateTime closeAt;

    public void update(PostUpdateDto postUpdateDto) {
        this.participantTotal = postUpdateDto.getParticipantTotal();
        this.contents = postUpdateDto.getContents();
        this.foodTypeTag = postUpdateDto.getFoodTypeTag();
        this.ageTag = postUpdateDto.getAgeTag();
        this.genderTag = postUpdateDto.getGenderTag();
        this.meetAt = postUpdateDto.getMeetAt();
        this.closeAt = postUpdateDto.getCloseAt();
    }


    public void addAsk(Ask ask) {
        asks.add(ask);
    }

    public void removeAsk(Ask ask) {
        asks.remove(ask);
    }

    public void addParticipantCount() {
        this.participantCount++;
        // todo : 신청 상태 변경 [수락->참여]
        if (asks.size() == this.participantTotal) {
            postStatus = PostStatus.COMPLETION;
        }
    }

    public void subParticipantCount() {
        this.participantCount--;
    }
}
