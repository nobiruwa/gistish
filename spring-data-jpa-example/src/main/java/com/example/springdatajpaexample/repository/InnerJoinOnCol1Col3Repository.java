package com.example.springdatajpaexample.repository;

import java.util.List;

import com.example.springdatajpaexample.entity.InnerJoinOnCol1Col3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InnerJoinOnCol1Col3Repository extends JpaRepository<InnerJoinOnCol1Col3, String> {
    @Query("SELECT new com.example.springdatajpaexample.entity.InnerJoinOnCol1Col3(t1.col1, t1.col2, t2.col3, t2.col4) FROM Table1 t1 INNER JOIN Table2 t2 ON t1.col1 = t2.col3")
    List<InnerJoinOnCol1Col3> findJoin();
}
