package edu.sdccd.cisc191.client;

import org.springframework.context.ApplicationContext;

public class SpringContext {
    private static ApplicationContext context;

    // Call this once during startup
    public static void setContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    // Use this anywhere to get a bean
    public static <T> T getBean(Class<T> beanClass) {
        if (context == null) {
            throw new IllegalStateException("Spring Context has not been initialized yet!");
        }
        return context.getBean(beanClass);
    }
}