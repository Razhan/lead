package com.ef.newlead.data.model;

/**
 * Created by seanzhou on 8/12/16.
 */
public class City {
    private final String pinyin;
    private final String name;
    private final String code;

    public City(String pinyin, String name, String code) {
        this.pinyin = pinyin;
        this.name = name;
        this.code = code;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getFullName(){
        return  getName() + " " + getPinyin();
    }

    @Override
    public String toString() {
        return "City{" +
                "pinyin='" + pinyin + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
