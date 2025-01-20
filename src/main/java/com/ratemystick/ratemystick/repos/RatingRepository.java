package com.ratemystick.ratemystick.repos;

import com.ratemystick.ratemystick.domain.Post;
import com.ratemystick.ratemystick.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RatingRepository extends JpaRepository<Rating, Long> {

    Rating findFirstByPost(Post post);

}
