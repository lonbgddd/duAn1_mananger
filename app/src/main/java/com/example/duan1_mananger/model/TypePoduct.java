package com.example.duan1_mananger.model;

import java.io.Serializable;

public class TypePoduct implements Serializable {
    private long id;
    private String name_type;

    public TypePoduct() {
    }

    public TypePoduct(long id, String name_type) {
        this.id = id;
        this.name_type = name_type;
    }

    public TypePoduct(String name_type) {
        this.name_type = name_type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName_type() {
        return name_type;
    }

    public void setName_type(String name_type) {
        this.name_type = name_type;
    }
}
