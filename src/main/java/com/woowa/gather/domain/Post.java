package com.woowa.gather.domain;

import com.woowa.gather.domain.enums.Age;
import com.woowa.gather.domain.enums.FoodType;
import com.woowa.gather.domain.enums.Gender;
import com.woowa.gather.domain.enums.PostStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
    private int participantTotal;
    private int participantCount;
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
    private LocalDateTime meetAt;
    private LocalDateTime closeAt;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
