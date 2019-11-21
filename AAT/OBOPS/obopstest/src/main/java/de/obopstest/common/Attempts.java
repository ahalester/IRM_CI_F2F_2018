package de.obopstest.common;

public class Attempts {

    public static void main(String[] args) {
        SessionStateHandler.setValue("a","aaa");
        SessionStateHandler.setValue("b","bbb");
        SessionStateHandler.setValue("c","ccc");
        SessionStateHandler.setValue("d","ddd");

        System.out.println(SessionStateHandler.getAllKeys());
    }
}
