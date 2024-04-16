package com.woowa.webpush.domain;

import com.woowa.common.domain.BaseEntity;
import com.woowa.gather.domain.Post;
import jakarta.persistence.*;

@Entity
public class WebPush extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long push_id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
