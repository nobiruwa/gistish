package com.example.springdatajpaexample.service;

import java.util.List;

import com.example.springdatajpaexample.entity.InnerJoinOnCol1Col3;
import com.example.springdatajpaexample.repository.InnerJoinOnCol1Col3Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InnerJoinOnCol1Col3Service {
    @Autowired
    private InnerJoinOnCol1Col3Repository leftJoinOnCol1Col3Repository;

    public List<InnerJoinOnCol1Col3> findAll() {
        return leftJoinOnCol1Col3Repository.findJoin();
    }
}
