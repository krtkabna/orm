package com.wasp.orm.querygenerator;

import com.wasp.orm.querygenerator.annotation.Column;
import com.wasp.orm.querygenerator.exception.WaspOrmRuntimeException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.StringJoiner;

import static com.wasp.orm.querygenerator.QueryGeneratorUtils.checkIllegalArgument;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getColumnName;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getFieldValue;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getIdFieldValue;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getIdString;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getTableName;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.isColumn;

public class SQLQueryGenerator implements QueryGenerator {
    private static final String SELECT = "SELECT ";
    private static final String FROM = " FROM ";
    private static final String WHERE = " WHERE ";

    @Override
    public String findAll(Class<?> clazz) {
        checkIllegalArgument(clazz);
        StringBuilder result = new StringBuilder(SELECT);

        result.append(getParametersString(clazz))
            .append(FROM)
            .append(getTableName(clazz))
            .append(";");
        return result.toString();
    }

    @Override
    public String findById(Serializable id, Class<?> clazz) {
        checkIllegalArgument(clazz);

        StringBuilder result = new StringBuilder(SELECT);

        result.append(getParametersString(clazz))
            .append(FROM)
            .append(getTableName(clazz))
            .append(WHERE)
            .append(getIdString(clazz))
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
            .append(getIdString(clazz))
            .append("=")
            .append(getIdFieldValue(value))
            .append(";");

        return result.toString();
    }

    private String getParametersString(Class<?> clazz) {
        StringJoiner parameters = new StringJoiner(", ");

        for (Field declaredField : clazz.getDeclaredFields()) {
            if (isColumn(declaredField)) {
                parameters.add(getColumnName(declaredField));
            }
        }

        return parameters.toString();
    }

    private String getObjectFieldValues(Object o) {
        StringJoiner arguments = new StringJoiner(", ", "(", ")");

        for (Field declaredField : o.getClass().getDeclaredFields()) {
            if (isColumn(declaredField)) {
                arguments.add(getFieldValue(declaredField, o));
            }
        }

        return arguments.toString();
    }
}