[![Build Status](https://secure.travis-ci.org/rmannibucau/cdi-spring-bridge.png)](http://travis-ci.org/rmannibucau/cdi-spring-bridge)

# Goal

Be able to reuse spring beans in CDI application.

# Usage
## Write an application context producer

    public class AppCtxProducer {
        @Produces
        public ConfigurableApplicationContext create() {
            return new ClassPathXmlApplicationContext("app-ctx.xml");
        }

        public void close(@Disposes final ConfigurableApplicationContext ctx) {
            ctx.close();
        }
    }

## Inject your spring beans in your CDI beans

    @Spring
    @Inject
    private SomeSpringStuff bean;

