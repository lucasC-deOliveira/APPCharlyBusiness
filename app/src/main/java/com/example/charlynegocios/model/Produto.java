package com.example.charlynegocios.model;

import android.net.Uri;

import java.io.Serializable;

public class Produto implements Serializable {

    private String UID;
    private String nome;
    private Double preco;
    private String uriFoto;


    public Produto(){

    }


    public Produto(String UID, String nome, Double preco, String uriFoto) {
        this.UID = UID;
        this.nome = nome;
        this.preco = preco;
        this.uriFoto= uriFoto;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getUriFoto() {
        return uriFoto;
    }

    public void setUriFoto(String uriFoto) {
        this.uriFoto = uriFoto;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "UID='" + UID + '\'' +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", uriFoto=" + uriFoto +
                '}';
    }
}
