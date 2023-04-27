package com.fa.ims.util;


import java.util.Arrays;


public final class EnumUtils {
    private EnumUtils() {}

    public static <T> T getEnumByName(Class<T> clazz, String name) {
        return Arrays.stream(clazz.getEnumConstants()).filter(t -> t.toString().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
