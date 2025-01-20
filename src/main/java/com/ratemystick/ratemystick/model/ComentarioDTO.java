package com.ratemystick.ratemystick.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class ComentarioDTO {

    private Long id;

    @Size(max = 255)
    private String contenido;

    @NotNull
    private Long usuario;

    @NotNull
    private Long post;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(final String contenido) {
        this.contenido = contenido;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(final Long usuario) {
        this.usuario = usuario;
    }

    public Long getPost() {
        return post;
    }

    public void setPost(final Long post) {
        this.post = post;
    }

}
