package com.woowa.webpush;

import com.woowa.gather.domain.Post;
import jakarta.persistence.*;

@Entity
public class WebPush {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int push_id;

    @JoinColumn(name = "post_id")
    @ManyToOne
    private Post post;
}
