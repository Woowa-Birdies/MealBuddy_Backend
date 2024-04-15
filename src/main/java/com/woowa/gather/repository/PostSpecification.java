package com.woowa.gather.repository;

import com.woowa.gather.domain.Location;
import com.woowa.gather.domain.Post;
import com.woowa.gather.domain.enums.Age;
import com.woowa.gather.domain.enums.FoodType;
import com.woowa.gather.domain.enums.Gender;
import com.woowa.gather.domain.enums.PostStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PostSpecification {

    public static Specification<Post> findPostsByPlaceContaining(String place){
        return new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join<Post, Location> locationJoin = root.join("location", JoinType.INNER);
                return criteriaBuilder.like(locationJoin.get("place"), "%" + place + "%");
            }
        };
    }

    public static Specification<Post> findPostsByAddressContaining(String address){
        return new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join<Post, Location> locationJoin = root.join("location", JoinType.INNER);
                return criteriaBuilder.like(locationJoin.get("address"), "%" + address + "%");
            }
        };
    }

    public static Specification<Post> findPostsBetweenDates(LocalDateTime startDate, LocalDateTime endDate){
        return new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get("meetAt"), startDate, endDate);
            }
        };
    }

    public static Specification<Post> findPostsEqualPostStatus(PostStatus postStatus) {
        return new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("postStatus"), postStatus);
            }
        };
    }

    public static Specification<Post> findPostsEqualFoodType(FoodType foodType) {
        return new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("foodTypeTag"), foodType);
            }
        };
    }

    public static Specification<Post> findPostsEqualAge(Age age) {
        return new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("ageTag"), age);
            }
        };
    }

    public static Specification<Post> findPostsEqualGender(Gender gender) {
        return new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("genderTag"), gender);
            }
        };
    }
}
