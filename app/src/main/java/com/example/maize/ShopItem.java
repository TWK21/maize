package com.example.maize;

public class ShopItem {

    private String name;
    private int Xa, Ya;
    private double Xb, Yb;
    private int id;
    private String description;
    public ShopItem(String name, int Xa, int Ya, double Xb, double Yb, String description, int id) {
        this.name = name;
        this.Xa = Xa;
        this.Xb = Xb;
        this.Ya = Ya;
        this.Yb = Yb;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getXa() {
        return Xa;
    }

    public double getXb() {
        return Xb;
    }

    public int getYa() {
        return Ya;
    }

    public double getYb() {
        return Yb;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
