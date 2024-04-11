package com.woowa.gather.domain;

import com.woowa.common.domain.BaseEntity;
import com.woowa.gather.domain.enums.AskStatus;
import com.woowa.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ask extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'WAITING'")
    private AskStatus askStatus;

    public static Ask createAsk(Post post, User user) {
        Ask ask = Ask.builder()
                .post(post)
                .user(user)
                .build();
        post.addAsk(ask);
        return ask;
    }

    public void changeAskStatus(AskStatus askStatus) {
        this.askStatus = askStatus;
    }

    public void changeAskStatusToParticipation() {
        this.askStatus = AskStatus.PARTICIPATION;
    }
}
