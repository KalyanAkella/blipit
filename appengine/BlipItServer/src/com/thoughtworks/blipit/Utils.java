/*
 * Copyright (c) 2010 BlipIt Committers
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

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

    public static BlipItError getBlipItError(String message) {
        BlipItError blipItError = new BlipItError();
        blipItError.setMessage(message);
        return blipItError;
    }

    public static interface Task<T> {
        void perform(T arg);
    }
}
