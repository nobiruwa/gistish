package javaee7.jsf.example;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named
public class SessionScopedBean implements Serializable {
    private static final long serialVersionUID = 5843985777064964986L;
    private RandomString randomString;
    private String name;

    @Inject
    private ApplicationScopedBean applicationScopedBean;

    @PostConstruct
    public void init() {
        randomString = new RandomString();
        setRandomName();
    }

    @PreDestroy
    public void destroy() {
        applicationScopedBean.appendLog(new Date() + " " + this.getClass().getName() + " destroyed.");
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
}
