package com.wasp.orm.querygenerator;

import com.wasp.orm.querygenerator.entity.Person;
import com.wasp.orm.querygenerator.entity.TestEntity;
import com.wasp.orm.querygenerator.exception.NoSuchFieldRuntimeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class QueryGeneratorTests {
    QueryGenerator queryGenerator = new DefaultQueryGenerator();

    @Test
    public void findAllTest() {
        String expectedQuery = "SELECT person_id, name, age FROM Person;";
        String actualQuery = queryGenerator.findAll(Person.class);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void findByIdTest() {
        String expectedQuery = "SELECT person_id, name, age FROM Person WHERE id=1;";
        String actualQuery = queryGenerator.findById(1, Person.class);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void insertTest() {
        Person person = new Person();
        person.setId(27);

        String expectedQuery = "INSERT INTO Person VALUES(27, 'Harry', 42);";
        String actualQuery = queryGenerator.insert(person);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void deleteTest() {
        Person person = new Person();
        person.setId(27);

        String expectedQuery = "DELETE FROM Person WHERE id=27;";
        String actualQuery = queryGenerator.delete(person);

        assertEquals(expectedQuery, actualQuery);
    }
}
