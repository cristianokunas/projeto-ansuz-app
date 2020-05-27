package com.anusuz.modelo;

import com.google.firebase.database.Exclude;

public class Usuario {
    //preciamos implementar serializable para poder passar o objeto entre activits
    private String id;
    private String email;
    private String senha;

    private static Usuario user;

    public Usuario() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
