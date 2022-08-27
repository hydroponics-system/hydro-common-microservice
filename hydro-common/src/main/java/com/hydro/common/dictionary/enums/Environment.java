package com.hydro.common.dictionary.enums;

import java.util.Arrays;

import org.springframework.util.Assert;

/**
 * Enum to map environments to objects.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
public enum Environment implements TextEnum {
    PRODUCTION("P"),
    DEVELOPMENT("D"),
    TEST("T"),
    LOCAL("L");

    private String textId;

    private Environment(String textId) {
        this.textId = textId;
    }

    @Override
    public String getTextId() {
        return textId;
    }

    /**
     * Will get the environment object enum from the passed in text value. If the
     * enum is invalid it will return the {@link Environment#LOCAL} environment by
     * default.
     * 
     * @param text The text to process.
     * @return {@link Environment} Object
     */
    public static Environment get(String text) {
        Assert.notNull(text, "Text ID can not be null");
        return Arrays.asList(Environment.values()).stream().filter(e -> e.getTextId().equals(text.toUpperCase()))
                .findAny().orElse(LOCAL);
    }
}
