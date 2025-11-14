package com.ringkhang.freewill.models;



public class Temp {
    private String name;
    private String email;


    public Temp() {
    }

    public Temp(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Temp{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
