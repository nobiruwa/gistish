package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.service.HistoricProcessInstanceVariablesService;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ProcessRunningTest.EmptyApplicationContext.class })
public class ProcessRunningTest {
    @Configuration
    @ComponentScan(basePackages = { "com.example" })
    static class EmptyApplicationContext {
    }

    @Autowired
    private HistoricProcessInstanceVariablesService historicProcessInstanceVariablesService;

    private ProcessEngine processEngine;

    @BeforeEach
    public void before() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        processEngine = configuration.buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();

        repositoryService.createDeployment().addClasspathResource("com/example/OneServiceTask.bpmn").deploy();
    }

    @Test
    public void givenDeployedProcess_whenStartProcessInstance_thenRunning() {
        Date date = new Date(System.currentTimeMillis());
        //deploy the process definition
        Map<String, Object> variables = new HashMap<>();
        variables.put("greeting", "");

        HistoryService historyService = processEngine.getHistoryService();
        Long previousCount = historyService.createHistoricProcessInstanceQuery().count();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess", variables);

        Long count = historyService.createHistoricProcessInstanceQuery().count();

        assertEquals(1, count - previousCount);

        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().list();
        historicProcessInstanceList.forEach((instance) -> System.out.println(instance.getProcessVariables().size()));

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();

        assertTrue(historicProcessInstance.getStartTime().after(date));

        Map<String, Object> completedProcessVariables = historicProcessInstance.getProcessVariables();
        // Doubt!
        // assertEquals(1, completedProcessVariables.size());
        // assertEquals("Hello World!", completedProcessVariables.get("greeting"));

        assertEquals(0, completedProcessVariables.size());

        // assertEquals(1, historicProcessInstanceVariablesService.findAll().size(), "テーブルにレコードが1つある");
        // assertEquals("Hello World!", historicProcessInstanceVariablesService.findAll().get(0).getText(), "1番目のレコードの値がHello World!である");

        assertEquals(1, historicProcessInstanceVariablesService.findByProcInstId(historicProcessInstance.getId()).size(), "プロセスインスタンスIDをキーにレコードが取得できる");
        assertEquals("Hello World!", historicProcessInstanceVariablesService.findByProcInstId(historicProcessInstance.getId()).get(0).getText(), "レコードの値がHello World!である");

        Date endTime = historicProcessInstance.getEndTime();
        assertTrue(endTime.after(date));
    }
}
