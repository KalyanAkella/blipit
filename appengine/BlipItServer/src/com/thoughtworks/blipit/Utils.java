package com.thoughtworks.blipit;

import com.thoughtworks.contract.BlipItError;
import org.datanucleus.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {
    public static List<String> splitByComma(String stringWithCommas) {
        if (StringUtils.isEmpty(stringWithCommas)) return null;
        String[] strings = stringWithCommas.split("[\\s]*,[\\s]*");
        List<String> listOfStrs = new ArrayList<String>();
        for (String string : strings) {
            if (StringUtils.notEmpty(string)) listOfStrs.add(string);
        }
        return listOfStrs;
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    static BlipItError getBlipItError(String message) {
        BlipItError blipItError = new BlipItError();
        blipItError.setMessage(message);
        return blipItError;
    }
}
