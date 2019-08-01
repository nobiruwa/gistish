package com.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MySecondServiceTask implements JavaDelegate {

    @Autowired
    public SessionFactory sessionFactory;

    private Expression processEngineConfiguration;

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("execute");
        System.out.println(sessionFactory);
        System.out.println(processEngineConfiguration.getValue(execution));
        execution.setVariable("greeting", (String) execution.getVariable("greeting") + "!");
    }

}
