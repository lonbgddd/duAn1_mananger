package com.example.duan1_mananger.model;

public class Product {
    private int id ;
    private String name_product;
    private double price;
    private String describe;
    private TypePoduct typePoduct;
    private String imgProduct;
    private String note;


    public Product() {
    }

    public Product (String name_product, double price, TypePoduct typePoduct, String note) {
        this.name_product = name_product;
        this.price = price;

        this.typePoduct = typePoduct;
        this.note = note;
    }

    public Product(int id, String name_product, double price, String note, String describe, TypePoduct typePoduct, String imgProduct) {
        this.id = id;
        this.name_product = name_product;
        this.price = price;
        this.note = note;
        this.describe = describe;
        this.typePoduct = typePoduct;
        this.imgProduct = imgProduct;
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

    public TypePoduct getTypePoduct() {
        return typePoduct;
    }

    public void setTypePoduct(TypePoduct typePoduct) {
        this.typePoduct = typePoduct;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }
}
