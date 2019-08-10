package com.example.demo;

import javax.persistence.EntityManager;

import com.example.demo.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UserService implements CommandLineRunner {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void run(String... args) throws Exception {
        Random r = new Random();

        User user = new User();
        user.setUser("user01" + r.nextInt());
        entityManager.persist(user);

        System.out.println(entityManager.contains(user));
    }
}
