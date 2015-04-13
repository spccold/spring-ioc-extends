package com.geek.spring.extend;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.Assert;

import com.geek.spring.extend.dto.Dto;

public class ApplicationContextExtendTest {
    private static final String        RESOURCE_LOCATION = "spring.xml";
    private static final String        EXPECTED_NAME     = "DTO";
    private AbstractApplicationContext context           = null;

    @Before
    public void before() throws IOException {
        context = new ByteArrayXmlApplicationContext(new ByteArrayInputStream(loadResource()));
    }

    @Test
    public void testApplicationContext() {
        Assert.isTrue(EXPECTED_NAME.equals(context.getBean(Dto.class).getName()));
    }

    @After
    public void after() {
        context.close();
    }

    /**
     * 从classpath下加载spring配置，并转换成字节数组
     * 
     * @return
     * @throws IOException
     */
    private byte[] loadResource() throws IOException {
        ClassLoader cl = ApplicationContextExtendTest.class.getClassLoader();
        Enumeration<URL> resourceUrls = (cl != null ? cl.getResources(RESOURCE_LOCATION) : ClassLoader
            .getSystemResources(RESOURCE_LOCATION));
        if (resourceUrls.hasMoreElements()) {
            URL url = resourceUrls.nextElement();
            InputStream is = url.openStream();
            byte[] buf = new byte[12];
            int position = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((position = is.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, position);
            }
            return baos.toByteArray();
        }
        return null;
    }
}
