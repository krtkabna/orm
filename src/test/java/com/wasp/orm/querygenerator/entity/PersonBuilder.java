package com.wasp.orm.querygenerator.entity;

public class PersonBuilder {

    private Person person;

    public PersonBuilder() {
        person = new Person();
    }

    public static PersonBuilder newPerson() {
        return new PersonBuilder();
    }

    public static Person defaultPerson() {
        return (new PersonBuilder())
            .setName("Harry")
            .setAge(42)
            .setId(27)
            .build();
    }

    public PersonBuilder setId(int id) {
        person.setId(id);
        return this;
    }

    public PersonBuilder setName(String name) {
        person.setName(name);
        return this;
    }

    public PersonBuilder setAge(int age) {
        person.setAge(age);
        return this;
    }

    public Person build() {
        return person;
    }
}
