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

    //All the Geo points have the real co-ordinates, please keep it that way if you add more locations.
    static class Alerts {

        public static Alert NavarangTheatre() {
            return new Alert("Navarang", "Kannada Movie", new GeoPt(12.99800466383297f, 77.55291799658964f), Arrays.asList(Channels.Movie().getId()));
        }

        public static Alert FameLido() {
            return new Alert("Fame Lido", "Movie premier", new GeoPt(12.973881024116972f, 77.62282172793577f), Arrays.asList(Channels.Movie().getId()));
        }

        public static Alert PVRCinemas() {
            return new Alert("PVR", "Movie screening", new GeoPt(12.935131942393395f, 77.61056939715574f), Arrays.asList(Channels.Movie().getId()));
        }

        public static Alert MTR() {
            return new Alert("MTR", "Karnataka ethnic food", new GeoPt(12.955009113220616f, 77.58542100543211f), Arrays.asList(Channels.Food().getId()));
        }
    }

    static class Locations {

        public static GeoPt MGRoad() {
            return new GeoPt(12.97343146061112f, 77.61389533633421f);
        }

        public static GeoPt K3sHome() {
            return new GeoPt(13.007470526621729f, 77.5551978742523f);
        }
    }
}
