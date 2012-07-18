package com.github.rmannibucau.cdi.spring.internal;

import com.github.rmannibucau.cdi.spring.Spring;
import org.apache.deltaspike.core.util.bean.BaseImmutableBean;
import org.apache.deltaspike.core.util.metadata.AnnotationInstanceProvider;
import org.springframework.beans.factory.ListableBeanFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionTarget;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class SpringBean<T> extends BaseImmutableBean<T> {
    private static final Set<Class<? extends Annotation>> STEREOTYPES = new HashSet<Class<? extends Annotation>>();
    private static final Set<Annotation> QUALIFIERS = new HashSet<Annotation>() {{
        add(AnnotationInstanceProvider.of(Spring.class));
    }};

    private final Class<T> clazz;
    private final ListableBeanFactory appCtx;
    private final InjectionTarget<T> injectionTarget;

    public SpringBean(final ListableBeanFactory ctx, final Class<T> type, final String id, final InjectionTarget<T> it) {
        super(type, id, QUALIFIERS, ApplicationScoped.class,
                STEREOTYPES, types(type),
                false, false,
                it.getInjectionPoints(),
                "SpringBean{" + id + "}");
        appCtx = ctx;
        clazz = type;
        injectionTarget = it;
    }

    private static Set<Type> types(final Class<?> clazz) {
        final Set<Type> classes = new HashSet<Type>();
        Class<?> current = clazz;
        while (current != null && !Object.class.equals(current)) {
            classes.add(current);
            current = current.getSuperclass();
        }
        return classes;
    }

    @Override
    public T create(final CreationalContext<T> tCreationalContext) {
        final T t = appCtx.getBean(getName(), clazz);
        // postconstruct managed by spring
        injectionTarget.inject(t, tCreationalContext);
        return t;
    }

    @Override
    public void destroy(final T instance, final CreationalContext<T> tCreationalContext) {
        // predestroy managed by spring
        injectionTarget.dispose(instance);
        tCreationalContext.release();
    }
}
