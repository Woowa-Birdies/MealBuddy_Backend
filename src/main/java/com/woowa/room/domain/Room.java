package com.woowa.room.domain;

import com.woowa.common.domain.BaseEntity;
import com.woowa.gather.domain.Post;
import com.woowa.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class  Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    //추후 모집자 추가

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    private String roomName;

    @Builder
    public Room(User user, Post post, String roomName) {
        this.user = user;
        this.post = post;
        this.roomName = roomName;
    }
}
