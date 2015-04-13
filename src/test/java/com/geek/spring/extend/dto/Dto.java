package com.geek.spring.extend.dto;

public class Dto {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dto [name=" + name + "]";
    }
}
