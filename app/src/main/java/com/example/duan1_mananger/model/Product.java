package com.example.duan1_mananger.model;

public class Product {
    private int id ;
    private String name_product;
    private double price;
    private String mota;
    private TypePoduct type_poduct;
    private int image_product;
    private String note;

    public Product() {
    }

    public Product(int id ,String name_product, double price, String mota, TypePoduct type_poduct, int image_product) {
        this.id = id;
        this.name_product = name_product;
        this.price = price;
        this.mota = mota;
        this.type_poduct = type_poduct;
        this.image_product = image_product;
    }

    public Product(int id, String name_product, double price, String note, String mota, TypePoduct type_poduct, int image_product) {
        this.id = id;
        this.name_product = name_product;
        this.price = price;
        this.note = note;
        this.mota = mota;
        this.type_poduct = type_poduct;
        this.image_product = image_product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public TypePoduct getType_product() {
        return type_poduct;
    }

    public void setType_product(TypePoduct type_poduct) {
        this.type_poduct = type_poduct;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getImage_product() {
        return image_product;
    }

    public void setImage_product(int image_product) {
        this.image_product = image_product;
    }
}
