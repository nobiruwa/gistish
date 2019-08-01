package com.example;

import java.util.concurrent.atomic.AtomicReference;

import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.delegate.invocation.DelegateInvocation;
import org.activiti.engine.impl.interceptor.DelegateInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringAutowireDelegateInterceptor implements DelegateInterceptor {
    private static Object lockObject = new Object();
    private static AtomicReference<ApplicationContext> applicationContextReference = new AtomicReference<>();

    public SpringAutowireDelegateInterceptor() {
        setApplicationContext();
    }

    // private void setApplicationContext(ApplicationContext applicationContext) {
    //     if (applicationContext == null) {
    //         throw new AssertionError("context is null");
    //     }

    //     synchronized (SpringAutowireDelegateInterceptor.class) {
    //         if (applicationContextReference.get() == null) {

    //             ApplicationContext old = applicationContextReference.getAndSet(annotationConfigApplicationContext);

    //             if (old != null) {
    //                 throw new AssertionError("the context aware hook should invoke only once.");
    //             }
    //         }
    //     }
    // }

    private synchronized static void setApplicationContext() {
        if (applicationContextReference.get() == null) {
            ApplicationContext context = createApplicationContext();
            ApplicationContext old = applicationContextReference.getAndSet(context);

            if (old != null) {
                throw new AssertionError("the context aware hook should invoke only once.");
            }
        }
    }

    private static ApplicationContext createApplicationContext() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.scan("com.example");
        annotationConfigApplicationContext.refresh();

        return annotationConfigApplicationContext;
    }

    private static ApplicationContext getContext() {
        return applicationContextReference.get();
    }

    @Override
    public void handleInvocation(DelegateInvocation invocation) {
        invocation.getInvocationParameters();
        Object target = invocation.getTarget();

        if (target instanceof JavaDelegate) {
            getContext().getAutowireCapableBeanFactory().autowireBean(target);
        }

        invocation.proceed();
    }
}
