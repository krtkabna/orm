package com.wasp.orm.querygenerator;

import com.wasp.orm.querygenerator.entity.Person;
import com.wasp.orm.querygenerator.entity.PersonBuilder;
import com.wasp.orm.querygenerator.entity.TestEntity;
import com.wasp.orm.querygenerator.exception.WaspOrmRuntimeException;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static com.wasp.orm.querygenerator.QueryGeneratorUtils.checkIllegalArgument;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getColumnName;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getFieldValue;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getIdColumnName;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getIdFieldName;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getIdFieldValue;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getTableName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class QueryGeneratorTests {
    private static final String PERSON_FIND_BY_ID_FORMAT = "SELECT person_id, name, age FROM Person WHERE person_id=%s;";
    private static final String PERSON_INSERT_FORMAT = "INSERT INTO Person(person_id, name, age) VALUES(%s, '%s', %s);";
    private static final String PERSON_DELETE_FORMAT = "DELETE FROM Person WHERE person_id=%s;";
    private static final String NAME = "name";
    private static final String AGE = "age";

    QueryGenerator queryGenerator = new SQLQueryGenerator();
    Person person = PersonBuilder.defaultPerson();

    @Test
    public void findAllTest() {
        String expectedQuery = "SELECT person_id, name, age FROM Person;";
        String actualQuery = queryGenerator.findAll(Person.class);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void findByIdTest() {
        String expectedQuery = String.format(PERSON_FIND_BY_ID_FORMAT, person.getId());
        String actualQuery = queryGenerator.findById(person.getId(), Person.class);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void insertTest() {
        String expectedQuery = String.format(PERSON_INSERT_FORMAT, person.getId(), person.getName(), person.getAge());
        String actualQuery = queryGenerator.insert(person);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void deleteTest() {
        String expectedQuery = String.format(PERSON_DELETE_FORMAT, person.getId());
        String actualQuery = queryGenerator.delete(person);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void checkIllegalArgumentTest() {
        assertThrows(IllegalArgumentException.class, () -> checkIllegalArgument(TestEntity.class));
    }

    @Test
    public void getTableNameTest() {
        assertThrows(IllegalArgumentException.class, () -> getTableName(TestEntity.class));
    }

    @Test
    public void getColumnNameTest() {
        assertThrows(IllegalArgumentException.class, () -> getColumnName(TestEntity.class.getDeclaredField("name")));
    }

    @Test
    public void getFieldValueTest() {
        Field name = null;
        try {
            name = Person.class.getDeclaredField(NAME);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        assertEquals(String.format("'%s'", person.getName()), getFieldValue(name, person));
    }

    @Test
    public void getIdStringTest() {
        assertThrows(WaspOrmRuntimeException.class, () -> getIdFieldName(TestEntity.class));
        System.out.println(getIdFieldName(Person.class));
        assertEquals("person_id", getIdColumnName(Person.class));
    }

    @Test
    public void getIdFieldValueTest() {
        TestEntity testEntity = new TestEntity();
        assertThrows(WaspOrmRuntimeException.class, () -> getIdFieldValue(testEntity));

        assertEquals(String.valueOf(person.getId()), getIdFieldValue(person));
    }
}
