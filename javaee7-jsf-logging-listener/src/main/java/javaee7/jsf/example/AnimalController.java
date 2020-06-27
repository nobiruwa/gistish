package javaee7.jsf.example;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@RequestScoped
public class AnimalController {
    // 以下の2つを付けると、xhtmlから参照できるようになる
    @Produces
    @Named
    private String cat;

    @PostConstruct
    private void init() {
        this.cat = "meow";
    }

    // 以下の2つを付けると、xhtmlから参照できるようになる
    @Produces
    @Named
    public String getDog() {
        return "bow";
    }
}
