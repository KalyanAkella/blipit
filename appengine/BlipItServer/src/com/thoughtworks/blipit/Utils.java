package com.thoughtworks.blipit;

import org.datanucleus.util.StringUtils;

import java.util.ArrayList;
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
}
