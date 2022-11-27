package com.example.duan1_mananger.model;

import java.io.Serializable;

public class Table implements Serializable {
    private int id_table;
    private String name_table;
    private String status = "false";

    public int getId_table() {
        return id_table;
    }

    public void setId_table(int id_table) {
        this.id_table = id_table;
    }

    public String getName_table() {
        return name_table;
    }

    public void setName_table(String name_table) {
        this.name_table = name_table;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
