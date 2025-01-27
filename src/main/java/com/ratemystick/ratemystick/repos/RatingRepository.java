package com.ratemystick.ratemystick.repos;

import com.ratemystick.ratemystick.domain.Post;
import com.ratemystick.ratemystick.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RatingRepository extends JpaRepository<Rating, Long> {

    Rating findFirstByPost(Post post);

    long countByPost(Post post);

    List<Rating> findByPostId(Long postId);

}
