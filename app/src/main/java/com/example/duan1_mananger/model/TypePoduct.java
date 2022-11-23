package com.example.duan1_mananger.model;

import java.io.Serializable;

public class TypePoduct implements Serializable {
    private int id;
    private String name_type;

    public TypePoduct() {
    }

    public TypePoduct(int id, String name_type) {
        this.id = id;
        this.name_type = name_type;
    }

    public TypePoduct(String name_type) {
        this.name_type = name_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_type() {
        return name_type;
    }

    public void setName_type(String name_type) {
        this.name_type = name_type;
    }
}
