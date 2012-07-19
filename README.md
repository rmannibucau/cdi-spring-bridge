[![Build Status](https://secure.travis-ci.org/rmannibucau/cdi-spring-bridge.png)](http://travis-ci.org/rmannibucau/cdi-spring-bridge)

# Goal

Be able to reuse spring beans in CDI application.

# Usage

You have two ways to use this extension.

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

Note: this is pretty useful but not portable (even if it should work on main CDI implementation

## Create a specific application context

Create a classpath resource called rmannibucau-spring-cdi.xml and import your spring app
in this file. It will ignore produced ConfigurableApplicationContext and is portable.

## Inject your spring beans in your CDI beans

    @Spring
    @Inject
    private SomeSpringStuff bean;

