package com.example.springdatajpaexample.service;

import java.util.ArrayList;
import java.util.List;

import com.example.springdatajpaexample.entity.Table2;
import com.example.springdatajpaexample.repository.Table2Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Table2Service {
    @Autowired
    private Table2Repository table1Repository;

    public void save(Table2 table2) {
        table1Repository.save(table2);
    }

    public List<Table2> findAll() {
        List<Table2> list = new ArrayList<Table2>();
        table1Repository.findAll().forEach(list::add);
        return list;
    }

}
