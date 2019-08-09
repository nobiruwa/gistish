package com.example.dao;

import java.util.List;

import com.example.model.HistoricProcessInstanceVariables;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HistoricProcessInstanceVariablesDao extends JpaRepository<HistoricProcessInstanceVariables, String> {

    @Query("SELECT h FROM HistoricProcessInstanceVariables h WHERE h.id = :id")
    HistoricProcessInstanceVariables find(@Param("id") String id);

    @Query("SELECT h FROM HistoricProcessInstanceVariables h WHERE h.procInstId = :procInstId")
    List<HistoricProcessInstanceVariables> findByProcInstId(@Param("procInstId") String procInstId);

    @Query("SELECT h FROM HistoricProcessInstanceVariables h WHERE h.executionId = :executionId")
    List<HistoricProcessInstanceVariables> findByExecutionId(@Param("executionId") String executionId);

    @Query("SELECT h FROM HistoricProcessInstanceVariables h WHERE h.taskId = :taskId")
    List<HistoricProcessInstanceVariables> findByTaskId(@Param("taskId") String taskId);

    @Query("SELECT h FROM HistoricProcessInstanceVariables h")
    List<HistoricProcessInstanceVariables> findAll();
}
