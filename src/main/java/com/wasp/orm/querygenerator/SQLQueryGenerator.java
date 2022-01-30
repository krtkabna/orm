package com.wasp.orm.querygenerator;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.StringJoiner;

import static com.wasp.orm.querygenerator.QueryGeneratorUtils.checkIllegalArgument;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getColumnName;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getFieldValue;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getIdColumnName;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getIdFieldName;
import static com.wasp.orm.querygenerator.QueryGeneratorUtils.getIdFieldValue;
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

        result.append(getParametersString(clazz)).append(FROM).append(getTableName(clazz)).append(";");
        return result.toString();
    }

    @Override
    public String findById(Serializable id, Class<?> clazz) {
        checkIllegalArgument(clazz);

        return SELECT + getParametersString(clazz) + FROM + getTableName(clazz) + WHERE + getIdColumnName(clazz) + "=" + id + ";";
    }

    @Override
    public String insert(Object value) {
        Class<?> clazz = value.getClass();
        checkIllegalArgument(clazz);

        return "INSERT INTO " + getTableName(clazz) + "(" + getParametersString(clazz) + ")" + " VALUES" + getObjectFieldValues(value) + ";";
    }

    @Override
    public String delete(Object value) {
        Class<?> clazz = value.getClass();
        checkIllegalArgument(clazz);

        return "DELETE" + FROM + getTableName(clazz) + WHERE + getIdColumnName(clazz) + "=" + getIdFieldValue(value) + ";";
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