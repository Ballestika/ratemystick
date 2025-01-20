package com.ratemystick.ratemystick.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;


@Entity
public class Usuario {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String correo;

    @Column
    private String contrasena;

    @OneToMany(mappedBy = "usuario")
    private Set<Post> posts;

    @OneToMany(mappedBy = "usuario")
    private Set<Comentario> comentarios;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(final String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(final String contrasena) {
        this.contrasena = contrasena;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(final Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(final Set<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

}
