package com.wasp.orm.querygenerator;

import com.wasp.orm.querygenerator.annotation.Column;
import com.wasp.orm.querygenerator.annotation.Table;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.StringJoiner;

public class DefaultQueryGenerator implements QueryGenerator {

    @Override
    public String findAll(Class<?> clazz) {
        checkIllegalArgument(clazz);

        StringBuilder result = new StringBuilder("SELECT ");

        StringJoiner parameters = new StringJoiner(", ");

        for (Field declaredField : clazz.getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                String fieldName = getFieldName(clazz);
                parameters.add(fieldName);
            }
        }
        result.append(parameters)
            .append(" FROM ")
            .append(getTableName(clazz))
            .append(";");
        return result.toString();
    }

    @Override
    public String findById(Serializable id, Class<?> clazz) {
        return null;
    }

    @Override
    public String insert(Object value) {

        return null;
    }

    @Override
    public String delete(Object value) {
        Class<?> clazz = value.getClass();
        return null;
    }


    private void checkIllegalArgument(Class<?> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        if (tableAnnotation == null) {
            throw new IllegalArgumentException("");
        }
    }

    private String getTableName(Class<?> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        return !tableAnnotation.name().isEmpty() ? tableAnnotation.name() : clazz.getName();
    }

    private String getFieldName(Class<?> clazz) {
        Column columnAnnotation = clazz.getAnnotation(Column.class);
        return !columnAnnotation.name().isEmpty() ? columnAnnotation.name() : clazz.getName();
    }
}