package com.wasp.orm.querygenerator.entity;

public class TestEntity {
    String name;
    int age;

    public TestEntity() {
        this.name = "Test";
        this.age = 120;
    }

    public TestEntity(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
