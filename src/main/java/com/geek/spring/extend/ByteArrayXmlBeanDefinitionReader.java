package com.geek.spring.extend;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * 
 * @author 机冷
 * @version $Id: ByteArrayXmlBeanDefinitionReader.java, v 0.1 2015年4月13日 下午8:53:41 机冷 Exp $
 */
public class ByteArrayXmlBeanDefinitionReader extends XmlBeanDefinitionReader implements ByteArrayBeanDefinitionReader {

    /** Logger available to subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    private ErrorHandler errorHandler = new SimpleSaxErrorHandler(logger);

    private DocumentLoader documentLoader = new DefaultDocumentLoader();
    
    public ByteArrayXmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public int loadBeanDefinitions(ByteArrayInputStream... baises) throws BeanDefinitionStoreException {
        List<ByteArrayResource> resources = new ArrayList<ByteArrayResource>();
        ByteArrayResource resource = null;
        for (ByteArrayInputStream bais : baises) {
            resource = new ByteArrayResource(bais);
            resources.add(resource);
        }

        return loadBeanDefinitions(resources.toArray(new ByteArrayResource[0]));
    }

    /**
     * 逻辑和XmlBeanDefinitionReader的doLoadBeanDefinitions基本一样，只是在获取validationMode，reset一下inputStream
     * 因为inputstream为ByteArrayInputStream,在getValidationModeForResource解析过一次，所以要reset()
     * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader#doLoadBeanDefinitions(org.xml.sax.InputSource, org.springframework.core.io.Resource)
     */
    @Override
    protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource) throws BeanDefinitionStoreException {

        try {
            int validationMode = getValidationModeForResource(resource);
            //FIXME import 重置ByteArrayInputStream
            inputSource.getByteStream().reset();
            //解析ByteArrayInputStream,生成BeanDefinition
            Document doc = this.documentLoader.loadDocument(inputSource, getEntityResolver(), this.errorHandler,
                validationMode, isNamespaceAware());
            return registerBeanDefinitions(doc, resource);
        } catch (BeanDefinitionStoreException ex) {
            throw ex;
        } catch (SAXParseException ex) {
            throw new XmlBeanDefinitionStoreException(resource.getDescription(), "Line " + ex.getLineNumber()
                                                                                 + " in XML document from " + resource
                                                                                 + " is invalid", ex);
        } catch (SAXException ex) {
            throw new XmlBeanDefinitionStoreException(resource.getDescription(), "XML document from " + resource
                                                                                 + " is invalid", ex);
        } catch (ParserConfigurationException ex) {
            throw new BeanDefinitionStoreException(resource.getDescription(),
                "Parser configuration exception parsing XML from " + resource, ex);
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException(resource.getDescription(), "IOException parsing XML document from "
                                                                              + resource, ex);
        } catch (Throwable ex) {
            throw new BeanDefinitionStoreException(resource.getDescription(),
                "Unexpected exception parsing XML document from " + resource, ex);
        }

    }
}
