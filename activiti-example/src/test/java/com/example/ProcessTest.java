package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ProcessTest {
    @BeforeAll
    public static void prepare() throws Exception {
        try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(com.example.ContextConfiguration.class)) {
            applicationContext.scan("com.example");

            MySecondServiceTask t = new MySecondServiceTask();
            applicationContext.getAutowireCapableBeanFactory().autowireBean(t);
            System.out.println("BeforeAll");
            System.out.println(t.sessionFactory);
        }
    }

    @Test
    public void givenBPMN_whenDeployProcess_thenDeployed() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");

        ProcessEngine processEngine = configuration.buildProcessEngine();
        // ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        assertNotNull(processEngine);

        RepositoryService repositoryService = processEngine.getRepositoryService();

        Long previousCount = repositoryService.createProcessDefinitionQuery().count();

        repositoryService.createDeployment().addClasspathResource("com/example/OneServiceTask.bpmn").deploy();

        Long count = repositoryService.createProcessDefinitionQuery().count();

        assertEquals(1, count - previousCount);

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(null).latestVersion().singleResult();

        assertEquals("myProcess", processDefinition.getKey());
    }

    @Nested
    public class Deployment {
        private ProcessEngine processEngine;

        @BeforeEach
        public void before() {
            ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
            processEngine = configuration.buildProcessEngine();
            // processEngine = ProcessEngines.getDefaultProcessEngine();

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

            Date endTime = historicProcessInstance.getEndTime();
            assertTrue(endTime.after(date));
        }
    }
}
