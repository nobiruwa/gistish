package com.example.springdatajpaexample.service;

import java.util.List;

import com.example.springdatajpaexample.entity.LeftJoinOnCol1Col3;
import com.example.springdatajpaexample.repository.LeftJoinOnCol1Col3Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeftJoinOnCol1Col3Service {
    @Autowired
    private LeftJoinOnCol1Col3Repository leftJoinOnCol1Col3Repository;

    public List<LeftJoinOnCol1Col3> findAll() {
        return leftJoinOnCol1Col3Repository.findJoin();
    }
}
