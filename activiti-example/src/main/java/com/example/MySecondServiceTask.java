package com.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import javax.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MySecondServiceTask implements JavaDelegate {

    @Autowired
    public EntityManagerFactory entityManagerFactory;

    private Expression processEngineConfiguration;

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("execute");
        System.out.println(entityManagerFactory);
        System.out.println(processEngineConfiguration.getValue(execution));
        execution.setVariable("greeting", (String) execution.getVariable("greeting") + "!");
    }

}
