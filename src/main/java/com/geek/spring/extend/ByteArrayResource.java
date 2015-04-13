package com.geek.spring.extend;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.AbstractResource;

public class ByteArrayResource extends AbstractResource {
    private InputStream is;

    public ByteArrayResource() {

    }

    public ByteArrayResource(InputStream inputStream) {
        this.is = inputStream;
    }

    @Override
    public String getDescription() {
        return "ByteArrayResource";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.is;
    }

}
