package com.example.duan1_mananger.model;

public class Table {
    private int id_table;
    private String name_table;
    private Boolean status = false;

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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
