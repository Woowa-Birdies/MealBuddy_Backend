package com.woowa.gather.domain;

import com.woowa.common.domain.BaseEntity;
import com.woowa.gather.domain.enums.Age;
import com.woowa.gather.domain.enums.FoodType;
import com.woowa.gather.domain.enums.Gender;
import com.woowa.gather.domain.enums.PostStatus;
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

    @OneToOne(fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "post")
    private List<Ask> asks = new ArrayList<>();

    private LocalDateTime meetAt;
    private LocalDateTime closeAt;
}
