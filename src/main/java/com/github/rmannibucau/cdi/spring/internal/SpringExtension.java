package com.github.rmannibucau.cdi.spring.internal;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;
import java.util.Set;
import java.util.logging.Logger;

public class SpringExtension implements Extension {
    private static final Logger LOGGER = Logger.getLogger(SpringExtension.class.getName());

    private static final String RESOURCE_NAME = "rmannibucau-spring-cdi.xml";
    private static final String CLASSLOADER_RESOURCE = "/" + RESOURCE_NAME;
    private static final String CLASSPATH_RMANNIBUCAU_SPRING_CDI_XML = "classpath:" + RESOURCE_NAME;

    protected void addSpringBeansToCdi(@Observes final AfterBeanDiscovery abd, final BeanManager bm) {
        ConfigurableApplicationContext ctx = null;
        if (Thread.currentThread().getContextClassLoader().getResource(CLASSLOADER_RESOURCE) != null) {
            ctx = new ClassPathXmlApplicationContext(CLASSPATH_RMANNIBUCAU_SPRING_CDI_XML);
        } else {
            // get spring context --> needs @Produces
            final Set<Bean<?>> beans = bm.getBeans(ConfigurableApplicationContext.class);
            if (beans != null && beans.size() == 1) {
                final Bean<?> bean = bm.resolve(beans);
                ctx = (ConfigurableApplicationContext) bm.getReference(bean, ConfigurableApplicationContext.class, bm.createCreationalContext(null));
            }
        }

        if (ctx != null) {
            // add spring beans to cdi
            for (String id : ctx.getBeanDefinitionNames()) {
                final Class<Object> clazz = (Class<Object>) ctx.getType(id);
                final AnnotatedType<Object> at = bm.createAnnotatedType(clazz);
                final InjectionTarget<Object> injectionTarget = bm.createInjectionTarget(at);
                abd.addBean(new SpringBean<Object>(ctx, clazz, id, injectionTarget));
            }
        } else {
            LOGGER.severe("no application context found");
        }
    }
}
