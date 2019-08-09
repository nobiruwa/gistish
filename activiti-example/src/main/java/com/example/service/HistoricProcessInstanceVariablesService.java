package com.example.service;

import java.util.List;

import com.example.dao.HistoricProcessInstanceVariablesDao;
import com.example.model.HistoricProcessInstanceVariables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoricProcessInstanceVariablesService {
    @Autowired
    private HistoricProcessInstanceVariablesDao dao;

    public HistoricProcessInstanceVariables find(String id) {
        return dao.find(id);
    }

    public List<HistoricProcessInstanceVariables> findByProcInstId(String procInstId) {
        return dao.findByProcInstId(procInstId);
    }

    public List<HistoricProcessInstanceVariables> findByExecutionId(String executionId) {
        return dao.findByExecutionId(executionId);
    }

    public List<HistoricProcessInstanceVariables> findByTaskId(String taskId) {
        return dao.findByTaskId(taskId);
    }

    public List<HistoricProcessInstanceVariables> findAll() {
        return dao.findAll();
    }
}
