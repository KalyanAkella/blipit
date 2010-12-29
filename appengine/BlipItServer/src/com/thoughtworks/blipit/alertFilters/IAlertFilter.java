package com.thoughtworks.blipit.alertFilters;

import com.thoughtworks.blipit.domain.Blip;

import java.util.List;

public interface IAlertFilter {
    void apply(List<Blip> alerts);
}
