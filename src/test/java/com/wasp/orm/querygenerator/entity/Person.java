package com.wasp.orm.querygenerator.entity;

import com.wasp.orm.querygenerator.annotation.Column;
import com.wasp.orm.querygenerator.annotation.Id;
import com.wasp.orm.querygenerator.annotation.Table;
import java.util.Random;

@Table(name = "Person")
public class Person {
    Random random = new Random();

    @Id
    @Column(name = "person_id")
    private int id;

    @Column
    private String name;

    @Column
    private int age;


    public Person() {
        this("Harry", 42);
    }

    public Person(String name, int age) {
        this.id = random.nextInt();
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
