package com.example.springdatajpaexample;

import com.example.springdatajpaexample.entity.Table1;
import com.example.springdatajpaexample.entity.Table2;
import com.example.springdatajpaexample.service.InnerJoinOnCol1Col3Service;
import com.example.springdatajpaexample.service.LeftJoinOnCol1Col3Service;
import com.example.springdatajpaexample.service.RightJoinOnCol1Col3Service;
import com.example.springdatajpaexample.service.Table1Service;
import com.example.springdatajpaexample.service.Table2Service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.springdatajpaexample.repository")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner comanndLine(Table1Service table1Service, Table2Service table2Service, LeftJoinOnCol1Col3Service leftJoinOnCol1Col3Service, RightJoinOnCol1Col3Service rightJoinOnCol1Col3Service, InnerJoinOnCol1Col3Service innerJoinOnCol1Col3Service) {
        return (args) -> {
            initialize(table1Service, table2Service);
            table1(table1Service);
            table2(table2Service);
            leftJoin(leftJoinOnCol1Col3Service);
            rightJoin(rightJoinOnCol1Col3Service);
            innerJoin(innerJoinOnCol1Col3Service);
        };
    }

    private void initialize(Table1Service table1Service, Table2Service table2Service) {
        table1Service.save(createTable1("1", "A"));
        table1Service.save(createTable1("2", "B"));
        table1Service.save(createTable1("3", "C"));
        table2Service.save(createTable2("1", "a"));
        table2Service.save(createTable2("3", "c"));
        table2Service.save(createTable2("5", "e"));
    }

    private void table1(Table1Service table1Service) {
        table1Service.findAll().stream().forEach(System.out::println);
    }

    private void table2(Table2Service table2Service) {
        table2Service.findAll().stream().forEach(System.out::println);
    }

    private void leftJoin(
            LeftJoinOnCol1Col3Service leftJoinOnCol1Col3Service) {
        leftJoinOnCol1Col3Service.findAll().stream().forEach(System.out::println);
    }

    private void rightJoin(
            RightJoinOnCol1Col3Service rightJoinOnCol1Col3Service) {
        rightJoinOnCol1Col3Service.findAll().stream().forEach(System.out::println);
    }

    private void innerJoin(
            InnerJoinOnCol1Col3Service innerJoinOnCol1Col3Service) {
        innerJoinOnCol1Col3Service.findAll().stream().forEach(System.out::println);
    }

    public Table1 createTable1(String col1, String col2) {
        Table1 table1 = new Table1();
        table1.setCol1(col1);
        table1.setCol2(col2);
        return table1;
    }

    public Table2 createTable2(String col3, String col4) {
        Table2 table2 = new Table2();
        table2.setCol3(col3);
        table2.setCol4(col4);
        return table2;
    }
}
