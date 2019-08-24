package com.example.springdatajpaexample.service;

import java.util.ArrayList;
import java.util.List;

import com.example.springdatajpaexample.entity.Table1;
import com.example.springdatajpaexample.repository.Table1Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Table1Service {
    @Autowired
    private Table1Repository table1Repository;

    public void save(Table1 table1) {
        table1Repository.save(table1);
    }

    public List<Table1> findAll() {
        List<Table1> list = new ArrayList<Table1>();
        table1Repository.findAll().forEach(list::add);
        return list;
    }
}
