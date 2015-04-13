package com.geek.spring.extend;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.BeanDefinitionStoreException;
/**
 * 
 * 
 * 
 * @author 机冷
 * @version $Id: ByteArrayBeanDefinitionReader.java, v 0.1 2015年4月13日 下午8:03:17 机冷 Exp $
 */
public interface ByteArrayBeanDefinitionReader {

    /**
     * Load bean definitions from the specified bytes.
     * @param  bytes store in baoses
     * @return the number of bean definitions found
     * @throws BeanDefinitionStoreException in case of loading or parsing errors
     */
    int loadBeanDefinitions(ByteArrayInputStream... baises) throws BeanDefinitionStoreException;
}
