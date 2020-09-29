package javaee7.jsf.example;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import javaee7.jsf.example.http.CheckRequest;

@Named
@RequestScoped
public class ReloadController {
    private static final Logger logger = Logger.getLogger(ReloadController.class.getName());

    @Named
    @Produces
    public Logger getLogger() {
        return logger;
    }

    // h:commandButton/@action属性には以下の規約に従ったメソッドをバインドする
    // 1. 引数なし
    // 2. オブジェクトまたはビューの名称を返す
    @CheckRequest
    public String reload() {
        logger.log(Level.SEVERE, "reload");

        // indexビューに戻る
        return "index";
    }
}
