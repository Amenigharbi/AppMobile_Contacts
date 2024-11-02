package com.example.myapplication;

public class Contact {
    private int id; // Clé primaire
    private String nom;
    private String prenom;
    private String numero;

    // Constructeur avec id
    public Contact(int id, String prenom, String nom, String numero) {
        this.id = id; // Initialisation de la clé primaire
        this.prenom = prenom;
        this.nom = nom;
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", numero='" + numero + '\'' +
                '}';
    }

    public int getId() {
        return id; // Getter pour l'id
    }

    public String getPhoneNumber() {
        return numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumero() {
        return numero;
    }

    public void setPhoneNumber(String numero) {
        this.numero = numero;
    }
}
