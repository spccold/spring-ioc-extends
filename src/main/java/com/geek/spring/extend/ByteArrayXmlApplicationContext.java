package com.geek.spring.extend;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;

/**
 * byte array applicationcontext
 * 
 * 某些情况下下，并没有具体的spring配置文件，配置文件是动态拼接出来的
 * 这时可以把拼接出来的配置文件读到字节数组，再通过ByteArrayInputStream包装就可以通过ByteArrayXmlApplicationContext启动spring 容器了
 *
 * 
 * @author 机冷
 * @version $Id: ByteArrayXmlApplicationContext.java, v 0.1 2015年4月13日 下午7:43:47 机冷 Exp $
 */
public class ByteArrayXmlApplicationContext extends AbstractXmlApplicationContext {
    /**存放所有配置的字节*/
    private ByteArrayInputStream[] configBytes;

    public ByteArrayXmlApplicationContext() {
    }

    public ByteArrayXmlApplicationContext(ApplicationContext context) {
        super(context);
    }

    public ByteArrayXmlApplicationContext(ByteArrayInputStream bais) {
        this(new ByteArrayInputStream[] { bais }, true, null);
    }

    public ByteArrayXmlApplicationContext(ByteArrayInputStream... baises) {
        this(baises, true, null);
    }

    public ByteArrayXmlApplicationContext(ByteArrayInputStream[] baises, ApplicationContext parent)
                                                                                                   throws BeansException {
        this(baises, true, parent);
    }

    public ByteArrayXmlApplicationContext(ByteArrayInputStream[] baises, boolean refresh) throws BeansException {
        this(baises, refresh, null);
    }

    public ByteArrayXmlApplicationContext(ByteArrayInputStream[] baises, boolean refresh, ApplicationContext parent) {
        super(parent);
        this.configBytes = baises;
        if (refresh) {
            refresh();
        }
    }

    /**
     * 覆写AbstractXmlApplication中的loadBeanDefinitions实现，把XmlBeanDefinitionReader换成ByteArrayXmlBeanDefinitionReader
     * @see org.springframework.context.support.AbstractXmlApplicationContext#loadBeanDefinitions(org.springframework.beans.factory.support.DefaultListableBeanFactory)
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
        // Create a new ByteArrayXmlBeanDefinitionReader for the given BeanFactory.
        ByteArrayXmlBeanDefinitionReader beanDefinitionReader = new ByteArrayXmlBeanDefinitionReader(beanFactory);

        // Configure the bean definition reader with this context's
        // resource loading environment.
        beanDefinitionReader.setEnvironment(this.getEnvironment());
        beanDefinitionReader.setResourceLoader(this);
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

        // Allow a subclass to provide custom initialization of the reader,
        // then proceed with actually loading the bean definitions.
        initBeanDefinitionReader(beanDefinitionReader);
        loadBeanDefinitions(beanDefinitionReader);
    }

    @Override
    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
        ByteArrayXmlBeanDefinitionReader byteReader = (ByteArrayXmlBeanDefinitionReader) reader;
        if (null != configBytes) {
            //通过bytes加载spring配置
            byteReader.loadBeanDefinitions(configBytes);
        }
    }
}
