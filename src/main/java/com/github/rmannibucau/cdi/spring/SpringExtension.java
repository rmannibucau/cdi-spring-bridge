package com.github.rmannibucau.cdi.spring;

import org.springframework.context.ConfigurableApplicationContext;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;
import java.util.Set;

public class SpringExtension implements Extension {
    protected void addSpringBeansToCdi(@Observes final AfterBeanDiscovery abd, final BeanManager bm) {
        // get spring context --> needs @Produces
        final Set<Bean<?>> beans = bm.getBeans(ConfigurableApplicationContext.class);
        final Bean<?> bean = bm.resolve(beans);
        final ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) bm.getReference(bean, ConfigurableApplicationContext.class, bm.createCreationalContext(null));

        // add spring beans to cdi
        for (String id : ctx.getBeanDefinitionNames()) {
            final Class clazz = ctx.getType(id);
            final AnnotatedType at = bm.createAnnotatedType(clazz);
            final InjectionTarget<Object> injectionTarget = bm.createInjectionTarget(at);
            abd.addBean(new SpringBean<Object>(ctx, clazz, id, injectionTarget));
        }
    }
}
