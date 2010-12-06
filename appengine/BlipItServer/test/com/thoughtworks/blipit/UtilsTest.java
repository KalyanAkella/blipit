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
