package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ProcessDeploymentTest.EmptyApplicationContext.class })
public class ProcessDeploymentTest {
    @Configuration
    @ComponentScan(basePackages = { "com.example" })
    static class EmptyApplicationContext {
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

}
