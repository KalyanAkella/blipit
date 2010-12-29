package com.thoughtworks.blipit.domain;

public enum Category {
    NONE, PANIC, AD;

    public static Category convert(com.thoughtworks.contract.common.Category category) {
        switch (category.ordinal()) {
            case 0:
                return NONE;
            case 1:
                return PANIC;
            case 2:
                return AD;
            default:
                return NONE;
        }
    }
}
