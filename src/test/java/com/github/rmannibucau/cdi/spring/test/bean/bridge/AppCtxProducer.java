package com.github.rmannibucau.cdi.spring.test.bean.bridge;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

public class AppCtxProducer {
    @Produces
    public ConfigurableApplicationContext create() {
        return new ClassPathXmlApplicationContext("app-ctx.xml");
    }

    public void close(@Disposes final ConfigurableApplicationContext ctx) {
        ctx.close();
    }
}
