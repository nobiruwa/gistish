package javaee7.jsf.example;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

@ApplicationScoped
public class TestRepository {
    @Resource
    private DataSource dataSource;
}
