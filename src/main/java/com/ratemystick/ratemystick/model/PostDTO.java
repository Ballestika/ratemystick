package com.ratemystick.ratemystick.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class PostDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String imagen;

    @Size(max = 255)
    private String descripcion;

    @NotNull
    private Long usuario;

    private String nombreUsuario; // Nombre del autor

    private Double rating; // Campo para la valoración promedio

    private List<ComentarioDTO> comentarios; // Asegúrate de que esta sea una lista de ComentarioDTO

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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(final String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(final double likes) {
        this.rating = likes;
    }

    public List<ComentarioDTO> getComentarios() {
        return comentarios;
    }

    public void setComentarios(final List<ComentarioDTO> comentarios) {
        this.comentarios = comentarios;
    }
}
