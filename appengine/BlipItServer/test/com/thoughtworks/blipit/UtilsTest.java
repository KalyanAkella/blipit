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

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class UtilsTest {
    @Test
    public void testSplitByComma() {
        List<String> strings;
        strings = Utils.splitByComma("abc, def");
        assertThat(strings, is(notNullValue()));
        assertThat(strings.size(), is(2));
        assertThat(strings.get(0), is("abc"));
        assertThat(strings.get(1), is("def"));

        strings = Utils.splitByComma("  ,    abc, def   , ");
        assertThat(strings, is(notNullValue()));
        assertThat(strings.size(), is(2));
        assertThat(strings.get(0), is("abc"));
        assertThat(strings.get(1), is("def"));

        strings = Utils.splitByComma(",abc,def,");
        assertThat(strings, is(notNullValue()));
        assertThat(strings.size(), is(2));
        assertThat(strings.get(0), is("abc"));
        assertThat(strings.get(1), is("def"));

        strings = Utils.splitByComma(null);
        assertThat(strings, is(nullValue()));

        strings = Utils.splitByComma("");
        assertThat(strings, is(nullValue()));
    }
}
