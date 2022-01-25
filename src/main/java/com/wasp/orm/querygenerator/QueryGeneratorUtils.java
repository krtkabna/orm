package com.wasp.orm.querygenerator;

import com.wasp.orm.querygenerator.annotation.Column;
import com.wasp.orm.querygenerator.annotation.Id;
import com.wasp.orm.querygenerator.annotation.Table;
import com.wasp.orm.querygenerator.exception.WaspOrmRuntimeException;
import java.lang.reflect.Field;
import java.util.Arrays;

public class QueryGeneratorUtils {
    private static final String NO_SUCH_ELEMENT_EX_FORMAT = "No field is annotated with @Id for class %s";

    static void checkIllegalArgument(Class<?> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        if (tableAnnotation == null) {
            throw new IllegalArgumentException("Class is not annotated with @Table: " + clazz.getName());
        }
    }
    static String getTableName(Class<?> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        return !tableAnnotation.name().isEmpty() ? tableAnnotation.name() : clazz.getName();
    }

    static String getColumnName(Field field) {
        Column columnAnnotation = field.getAnnotation(Column.class);
        return !columnAnnotation.name().isEmpty() ? columnAnnotation.name() : field.getName();
    }

    static String getFieldValue(Field field, Object o) throws IllegalAccessException {
        if (!field.canAccess(o)) {
            field.setAccessible(true);
        }
        String fieldValue = field.get(o).toString();
        return field.getType().equals(String.class)
            ? String.format("'%s'", fieldValue)
            : fieldValue;
    }

    static String getIdString(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> field.getAnnotation(Id.class) != null)
            .findFirst()
            .map(Field::getName)
            .orElseThrow(() -> new WaspOrmRuntimeException(
                String.format(NO_SUCH_ELEMENT_EX_FORMAT, clazz.getName())));
    }

    static String getIdFieldValue(Object value) {
        Class<?> clazz = value.getClass();
        try {
            Field field = clazz.getDeclaredField(getIdString(clazz));
            return getFieldValue(field, value);
        } catch (NoSuchFieldException e) {
            throw new WaspOrmRuntimeException(String.format(NO_SUCH_ELEMENT_EX_FORMAT, clazz.getSimpleName()), e);
        } catch (IllegalAccessException e) {
            throw new WaspOrmRuntimeException(e.getMessage(), e);
        }
    }
}
