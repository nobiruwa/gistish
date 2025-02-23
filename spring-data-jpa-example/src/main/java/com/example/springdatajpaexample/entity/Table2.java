package com.example.springdatajpaexample.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Table2 {
    @Id
    private String col3;

    private String col4;

    public String getCol3() {
        return col3;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

    @Override
    public String toString() {
        return (new ToStringWrapper("table2", this)).toString();
    }
}
