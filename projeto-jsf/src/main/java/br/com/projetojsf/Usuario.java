package br.com.projetojsf;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String nome;
    private String email;
    private String senha;
    private String token;

    public Usuario() {}

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}