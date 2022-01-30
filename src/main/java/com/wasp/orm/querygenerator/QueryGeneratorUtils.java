package com.wasp.orm.querygenerator;

import com.wasp.orm.querygenerator.annotation.Column;
import com.wasp.orm.querygenerator.annotation.Id;
import com.wasp.orm.querygenerator.annotation.Table;
import com.wasp.orm.querygenerator.exception.WaspOrmRuntimeException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Function;

public final class QueryGeneratorUtils {
    private static final String NO_SUCH_ELEMENT_EX_FORMAT = "No field is annotated with @Id for class %s";

    static void checkIllegalArgument(Class<?> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        if (tableAnnotation == null) {
            throw new IllegalArgumentException("Class is not annotated with @Table: " + clazz.getName());
        }
    }

    static void checkIllegalArgument(Field declaredField) {
        if (!isColumn(declaredField)) {
            throw new IllegalArgumentException("Field is not annotated with @Column: " + declaredField.getName());
        }
    }

    static boolean isColumn(Field declaredField) {
        return declaredField.getAnnotation(Column.class) != null;
    }

    static String getTableName(Class<?> clazz) {
        checkIllegalArgument(clazz);
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        return !tableAnnotation.name().isEmpty() ? tableAnnotation.name() : clazz.getName();
    }

    static String getColumnName(Field declaredField) {
        checkIllegalArgument(declaredField);
        Column columnAnnotation = declaredField.getAnnotation(Column.class);
        return !columnAnnotation.name().isEmpty() ? columnAnnotation.name() : declaredField.getName();
    }

    static String getFieldValue(Field field, Object o) {
        if (!field.canAccess(o)) {
            field.setAccessible(true);
        }

        String fieldValue;
        try {
            fieldValue = field.get(o).toString();
        } catch (IllegalAccessException e) {
            throw new WaspOrmRuntimeException(e.getMessage(), e);
        }

        return field.getType().equals(String.class)
            ? String.format("'%s'", fieldValue)
            : fieldValue;
    }

    static String getId(Class<?> clazz, Function<? super Field, ? extends String> mapper) {
        return Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> field.getAnnotation(Id.class) != null)
            .findFirst()
            .map(mapper)
            .orElseThrow(() -> new WaspOrmRuntimeException(
                String.format(NO_SUCH_ELEMENT_EX_FORMAT, clazz.getName())));
    }

    static String getIdFieldName(Class<?> clazz) {
        return getId(clazz, Field::getName);
    }

    static String getIdColumnName(Class<?> clazz) {
        return getId(clazz, QueryGeneratorUtils::getColumnName);
    }

    static String getIdFieldValue(Object value) {
        Class<?> clazz = value.getClass();
        try {
            Field field = clazz.getDeclaredField(getIdFieldName(clazz));
            return getFieldValue(field, value);
        } catch (NoSuchFieldException e) {
            throw new WaspOrmRuntimeException(String.format(NO_SUCH_ELEMENT_EX_FORMAT, clazz.getSimpleName()), e);
        }
    }
}
