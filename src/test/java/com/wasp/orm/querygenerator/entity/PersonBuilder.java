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
            .setAge(42)
            .build();
    }

    public PersonBuilder setAge(int age) {
        person.setAge(age);
        return this;
    }

    public Person build() {
        return person;
    }
}
