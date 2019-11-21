package de.obopstest.common;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SessionStateHandler {

    private static HashMap<String, Object> state = new HashMap<String, Object>();

    public static <T> T getValue(String key) {
        if (state.containsKey(key)) {
            return (T)state.get(key);
        }
        return null;
    }

    public static <T> void setValue(String key, T value) {
        state.put(key, value);
    }

    public static void clear() {
        state =  new HashMap<String, Object>();
    }

    public static List<String> getAllKeys(){
        List<String> a = new ArrayList<String>();
        for(Object o : state.keySet()){
            a.add((String)o);
        }
        return a;
    }
}