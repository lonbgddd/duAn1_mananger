package com.example.duan1_mananger.model;

import java.io.Serializable;

public class TypeProduct implements Serializable {
    private String id;
    private String nameType;

    public TypeProduct() {
    }

    public TypeProduct(String id, String nameType) {
        this.id = id;
        this.nameType = nameType;
    }

    public TypeProduct(String name_type) {
        this.nameType = name_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }
}
