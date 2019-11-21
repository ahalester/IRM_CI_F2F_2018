package de.aqua.web.utils.enums;

/**
 * Created by bdumitru on 7/21/2017.
 */
public enum Elements {

    LABEL("label"),
    TEXTFIELD("textbox"),
    OPTLIST("combobox"),
    DATETIME("datebox"),
    CHECKBTN("checkbox"),
    RADIOBTN("radiobutton");

    private final String value;

    private Elements(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}