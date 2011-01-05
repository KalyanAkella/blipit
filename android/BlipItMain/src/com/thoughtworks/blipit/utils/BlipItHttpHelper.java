package com.thoughtworks.blipit.utils;

public class BlipItHttpHelper {
    private static BlipItHttpHelper instance = new BlipItHttpHelper();

    public static BlipItHttpHelper getInstance() {
        return instance;
    }

    private BlipItHttpHelper() {
    }
}
