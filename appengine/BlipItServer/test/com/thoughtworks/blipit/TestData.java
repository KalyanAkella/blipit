package com.thoughtworks.blipit;

import com.google.appengine.api.datastore.GeoPt;
import com.thoughtworks.blipit.domain.Alert;
import com.thoughtworks.contract.common.Channel;

import java.util.Arrays;

public class TestData {

    static class Channels {

        public static Channel Movie() {
            return new Channel("MoviesChannelId", "Movies", "Movie Description");
        }

        public static Channel Food() {
            return new Channel("FoodChannelId", "Food", "Food Description");
        }
    }

    static class Alerts {

        public static Alert FameMovie() {
            return new Alert("Fame Lido", "Movie premier", new GeoPt(1.0f, 1.0f), Arrays.asList(Channels.Movie().getId()));
        }

        public static Alert PVRMovie() {
            return new Alert("PVR", "Movie screening", new GeoPt(2.0f, 2.0f), Arrays.asList(Channels.Movie().getId()));
        }

        public static Alert MTRFood() {
            return new Alert("MTR", "Karnataka ethnic food", new GeoPt(3.0f, 3.0f), Arrays.asList(Channels.Food().getId()));
        }
    }

}
