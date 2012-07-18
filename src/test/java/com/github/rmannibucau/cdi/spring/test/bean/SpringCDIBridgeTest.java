package com.github.rmannibucau.cdi.spring.test.bean;

import com.github.rmannibucau.cdi.spring.SpringExtension;
import com.github.rmannibucau.cdi.spring.test.bean.bridge.AppCtxProducer;
import com.github.rmannibucau.cdi.spring.test.bean.cdi.MyCdiBean1;
import com.github.rmannibucau.cdi.spring.test.bean.spring.MySpringBean1;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class SpringCDIBridgeTest {
    @Inject
    private MyCdiBean1 cdi;

    @Deployment
    public static JavaArchive jar() {
        return ShrinkWrap.create(JavaArchive.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addPackage(SpringExtension.class.getPackage())
                .addAsServiceProvider(Extension.class, SpringExtension.class)
                .addPackage(AppCtxProducer.class.getPackage())
                .addPackage(MyCdiBean1.class.getPackage())
                .addPackage(MySpringBean1.class.getPackage());
    }

    @Test
    public void springInCdi() {
        assertNotNull(cdi);
        assertTrue(cdi.getSpring().startsWith(MySpringBean1.class.getName())); // proxy, here we depend on the cdi impl for the test
    }
}
