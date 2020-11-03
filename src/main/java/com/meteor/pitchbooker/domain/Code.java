package com.meteor.pitchbooker.domain;

public enum Code {
    GAELIC_FOOTBALL("GAELIC_FOOTBALL"),
    HURLING("HURLING"),
    CAMOGIE("CAMOGIE"),
    LADIES_FOOTBALL("LADIES_FOOTBALL");

    private String code;

    private Code(String code) { this.code = code;}
    public String getCode() {return code;}
}
