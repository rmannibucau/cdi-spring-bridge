package com.github.rmannibucau.cdi.spring.test.bean.cdi;

import com.github.rmannibucau.cdi.spring.Spring;
import com.github.rmannibucau.cdi.spring.test.bean.spring.MySpringBean1;

import javax.inject.Inject;

public class MyCdiBean1 {
    @Spring
    @Inject
    private MySpringBean1 spring;

    public String getSpring() {
        return spring.getClass().getName();
    }

    public MySpringBean1 bean() {
        return spring;
    }
}
