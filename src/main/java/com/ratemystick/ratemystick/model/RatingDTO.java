package com.ratemystick.ratemystick.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class RatingDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String puntuacion;

    @NotNull
    private Long post;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(final String puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Long getPost() {
        return post;
    }

    public void setPost(final Long post) {
        this.post = post;
    }

}
