package com.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class MySecondServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        execution.setVariable("greeting", (String) execution.getVariable("greeting") + "!");
    }

}
