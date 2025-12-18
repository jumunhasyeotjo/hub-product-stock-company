package com.jumunhasyeo.common.util;

public class KafkaUtil {

    public static String getClassName(String fullTypeName) {
        return fullTypeName.substring(fullTypeName.lastIndexOf('.') + 1);
    }
}
