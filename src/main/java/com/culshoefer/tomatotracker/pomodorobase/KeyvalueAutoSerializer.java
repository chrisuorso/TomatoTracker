package com.culshoefer.tomatotracker.pomodorobase;

import javax.json.JsonValue;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 01/09/16.
 * This class is responsible for JSON-serializing a configuration given to it.
 * JSONObjects are not supported. This is meant to represent a flat key-value-store thing
 */
public class KeyvalueAutoSerializer {
    public void put(String key, JsonValue val){
        throw new UnsupportedOperationException();
    }
    public JsonValue get(String key){
        throw new UnsupportedOperationException();
    }
    public void setToDefault(){
        throw new UnsupportedOperationException();
    }
    public void setToDefault(String key){
        throw new UnsupportedOperationException();
    }
}
