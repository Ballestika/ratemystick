package com.ratemystick.ratemystick.model;

import jakarta.validation.constraints.NotNull;


public class AnuncioDTO {

    private Long id;

    @NotNull
    private Long clicks;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getClicks() {
        return clicks;
    }

    public void setClicks(final Long clicks) {
        this.clicks = clicks;
    }

}
