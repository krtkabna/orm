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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
