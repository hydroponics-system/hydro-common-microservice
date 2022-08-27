package com.hydro.common.jwt.domain;

import com.hydro.common.dictionary.enums.TextEnum;

/**
 * Jwt type to confirm what type of jwt token is being used.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
public enum JwtType implements TextEnum {
    SYSTEM("SYSTEM_JWT"),
    WEB("WEB_JWT");

    private String textId;

    private JwtType(String textId) {
        this.textId = textId;
    }

    @Override
    public String getTextId() {
        return textId;
    }
}
