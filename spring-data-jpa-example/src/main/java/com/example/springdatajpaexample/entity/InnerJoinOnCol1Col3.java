package com.example.springdatajpaexample.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class InnerJoinOnCol1Col3 {
    private String col1; // <-> col3

    private String col2; // <-> col4

    @Id
    private String col3;

    private String col4;

    public InnerJoinOnCol1Col3() {
    }

    public InnerJoinOnCol1Col3(String col1, String col2, String col3, String col4) {
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
        this.col4 = col4;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

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
        return (new ToStringWrapper("innerjoin", this)).toString();
    }
}
