package com.ratemystick.ratemystick.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class PostDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String imagen;

    @Size(max = 255)
    private String descripcion;

    @NotNull
    private Long usuario;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(final String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(final Long usuario) {
        this.usuario = usuario;
    }

}
