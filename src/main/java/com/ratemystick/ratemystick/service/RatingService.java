package com.ratemystick.ratemystick.service;

import com.ratemystick.ratemystick.domain.Post;
import com.ratemystick.ratemystick.domain.Rating;
import com.ratemystick.ratemystick.model.RatingDTO;
import com.ratemystick.ratemystick.repos.PostRepository;
import com.ratemystick.ratemystick.repos.RatingRepository;
import com.ratemystick.ratemystick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final PostRepository postRepository;

    public RatingService(final RatingRepository ratingRepository,
            final PostRepository postRepository) {
        this.ratingRepository = ratingRepository;
        this.postRepository = postRepository;
    }

    public List<RatingDTO> findAll() {
        final List<Rating> ratings = ratingRepository.findAll(Sort.by("id"));
        return ratings.stream()
                .map(rating -> mapToDTO(rating, new RatingDTO()))
                .toList();
    }

    public RatingDTO get(final Long id) {
        return ratingRepository.findById(id)
                .map(rating -> mapToDTO(rating, new RatingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RatingDTO ratingDTO) {
        final Rating rating = new Rating();
        mapToEntity(ratingDTO, rating);
        return ratingRepository.save(rating).getId();
    }

    public void update(final Long id, final RatingDTO ratingDTO) {
        final Rating rating = ratingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ratingDTO, rating);
        ratingRepository.save(rating);
    }

    public void delete(final Long id) {
        ratingRepository.deleteById(id);
    }

    private RatingDTO mapToDTO(final Rating rating, final RatingDTO ratingDTO) {
        ratingDTO.setId(rating.getId());
        ratingDTO.setPuntuacion(rating.getPuntuacion());
        ratingDTO.setPost(rating.getPost() == null ? null : rating.getPost().getId());
        return ratingDTO;
    }

    private Rating mapToEntity(final RatingDTO ratingDTO, final Rating rating) {
        rating.setPuntuacion(ratingDTO.getPuntuacion());
        final Post post = ratingDTO.getPost() == null ? null : postRepository.findById(ratingDTO.getPost())
                .orElseThrow(() -> new NotFoundException("post not found"));
        rating.setPost(post);
        return rating;
    }

}
