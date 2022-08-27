package com.hydro.common.dictionary.enums;

/**
 * Enums for all the possible user roles.
 * 
 * @author Sam Butler
 * @since September 6, 2021
 */
public enum WebRole implements TextEnum {
    USER(1 , "USER"),
    SYSTEM_USER(2 , "SYSTEM_USER"),
    DEVELOPER(3 , "DEVELOPER"),
    ADMIN(4 , "ADMIN");

    private int rank;
    private String textId;

    WebRole(int rank, String textId) {
        this.rank = rank;
        this.textId = textId;
    }

    /**
     * This is the rank the webrole holds.
     * 
     * @return {@link Integer} of the webroles rank
     */
    public int getRank() {
        return rank;
    }

    @Override
    public String getTextId() {
        return textId;
    }
}