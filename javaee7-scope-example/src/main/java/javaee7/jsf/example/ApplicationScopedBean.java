package javaee7.jsf.example;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
@Named
public class ApplicationScopedBean {
    private RandomString randomString;

    private List<String> logger;
    private String name;

    @PostConstruct
    public void init() {
        logger = new ArrayList<String>();
        randomString = new RandomString();
        setRandomName();
    }

    public List<String> getLogger() {
        return logger;
    }

    public void setLogger(List<String> logger) {
        this.logger = logger;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRandomName() {
        this.name = randomString.generate(5);
    }

    public void doNothing() {
    }

    public void appendLog(String logMessage) {
        logger.add(logMessage);
    }
}
