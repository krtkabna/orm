package com.wasp.orm.querygenerator;

import com.wasp.orm.querygenerator.entity.Person;
import com.wasp.orm.querygenerator.entity.PersonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryGeneratorTests {
    private static final String DEFAULT_QUERY = "";
    QueryGenerator queryGenerator = new SQLQueryGenerator();

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
        String expectedQueryFormat = "INSERT INTO Person VALUES(27, 'Harry', %s);";
        Person person = PersonBuilder.newPerson()
            .setAge(42)
            .build();
        String actualQuery = queryGenerator.insert(person);

        assertEquals(String.format(expectedQueryFormat, person.getAge()), actualQuery);
    }

    @Test
    public void deleteTest() {
        String expectedQuery = "DELETE FROM Person WHERE id=27;";
//        String actualQuery = queryGenerator.delete(person);

//        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    void test() {

    }
}
