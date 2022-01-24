package com.wasp.orm.querygenerator;

import com.wasp.orm.querygenerator.annotation.Column;
import com.wasp.orm.querygenerator.annotation.Id;
import com.wasp.orm.querygenerator.annotation.Table;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.StringJoiner;

public class DefaultQueryGenerator implements QueryGenerator {
    private static final String NO_SUCH_ELEMENT_EX_FORMAT = "No field is annotated with @Id for class %s";
    private static final String SELECT = "SELECT ";
    private static final String FROM = " FROM ";
    private static final String WHERE = " WHERE ";

    @Override
    public String findAll(Class<?> clazz) {
        checkIllegalArgument(clazz);
        StringBuilder result = new StringBuilder(SELECT);

        result.append(getParameters(clazz))
            .append(FROM)
            .append(getTableName(clazz))
            .append(";");
        return result.toString();
    }

    @Override
    public String findById(Serializable id, Class<?> clazz) {
        checkIllegalArgument(clazz);

        StringBuilder result = new StringBuilder(SELECT);

        result.append(getParameters(clazz))
            .append(FROM)
            .append(getTableName(clazz))
            .append(WHERE)
            .append(getIdStringAndHandleException(clazz))
            .append("=")
            .append(id)
            .append(";");

        return result.toString();
    }

    @Override
    public String insert(Object value) {
        Class<?> clazz = value.getClass();
        checkIllegalArgument(clazz);
        StringBuilder result = new StringBuilder("INSERT INTO ");

        result
            .append(getTableName(clazz))
            .append(" VALUES")
            .append(getObjectFieldValues(value))
            .append(";");

        return result.toString();
    }

    @Override
    public String delete(Object value) {
        Class<?> clazz = value.getClass();
        checkIllegalArgument(clazz);
        StringBuilder result = new StringBuilder("DELETE");

        result
            .append(FROM)
            .append(getTableName(clazz))
            .append(WHERE)
            .append(getIdStringAndHandleException(clazz))
            .append("=")
            .append(getIdFieldValue(value))
            .append(";");

        return result.toString();
    }

    private void checkIllegalArgument(Class<?> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        if (tableAnnotation == null) {
            throw new IllegalArgumentException("Class is not annotated with @Table: " + clazz.getName());
        }
    }

    private StringJoiner getParameters(Class<?> clazz) {
        StringJoiner parameters = new StringJoiner(", ");

        for (Field declaredField : clazz.getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                String fieldName = getFieldName(declaredField);
                parameters.add(fieldName);
            }
        }

        return parameters;
    }

    private String getFieldValue(Field field, Object o) throws IllegalAccessException {
        if (!field.canAccess(o)) {
            field.setAccessible(true);
        }
        if (field.getType().equals(String.class)) {
            return String.format("'%s'", field.get(o).toString());
        } else {
            return field.get(o).toString();
        }
    }

    private String getObjectFieldValues(Object o) {
        StringJoiner arguments = new StringJoiner(", ", "(", ")");

        String fieldValue;
        for (Field declaredField : o.getClass().getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                try {
                    fieldValue = getFieldValue(declaredField, o);
                    arguments.add(fieldValue);
                } catch (IllegalAccessException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        return arguments.toString();
    }

    private String getIdFieldValue(Object value) {
        Class<?> clazz = value.getClass();
        try {
            Field field = clazz.getDeclaredField(getIdString(clazz));
            return getFieldValue(field, value);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(String.format(NO_SUCH_ELEMENT_EX_FORMAT, clazz.getSimpleName()));
        } catch (IllegalAccessException e) {
           throw new RuntimeException(e.getMessage());
        }
    }

    private String getTableName(Class<?> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        return !tableAnnotation.name().isEmpty() ? tableAnnotation.name() : clazz.getName();
    }

    private String getFieldName(Field field) {
        Column columnAnnotation = field.getAnnotation(Column.class);
        return !columnAnnotation.name().isEmpty() ? columnAnnotation.name() : field.getName();
    }

    private String getIdString(Class<?> clazz) throws NoSuchFieldException {
        Optional<Field> idFieldOptional = Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> field.getAnnotation(Id.class) != null)
            .findAny();
        if (idFieldOptional.isPresent()) {
            return idFieldOptional.get().getName();
        } else {
            throw new NoSuchFieldException(String.format(NO_SUCH_ELEMENT_EX_FORMAT, clazz.getName()));
        }
    }

    private String getIdStringAndHandleException(Class<?> clazz) {
        try {
            return getIdString(clazz);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}