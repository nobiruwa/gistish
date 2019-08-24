package com.example.springdatajpaexample.service;

import java.util.List;

import com.example.springdatajpaexample.entity.RightJoinOnCol1Col3;
import com.example.springdatajpaexample.repository.RightJoinOnCol1Col3Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RightJoinOnCol1Col3Service {
    @Autowired
    private RightJoinOnCol1Col3Repository leftJoinOnCol1Col3Repository;

    public List<RightJoinOnCol1Col3> findAll() {
        return leftJoinOnCol1Col3Repository.findJoin();
    }
}
