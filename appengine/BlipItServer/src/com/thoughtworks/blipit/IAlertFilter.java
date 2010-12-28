package com.thoughtworks.blipit;

import com.thoughtworks.blipit.domain.Alert;

import java.util.List;

public interface IAlertFilter {
    void apply(List<Alert> alerts);
}
