package com.example.alpha;

public class Users {

    private String user;
    private String email;
    private int image;

    public Users(String user, String email , int image) {
        this.user = user;
        this.email = email;
        this.image = image;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String nome) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImage() {
        return image;
    }

    public void setImage(String telefono) {
        this.image = image;
    }

}