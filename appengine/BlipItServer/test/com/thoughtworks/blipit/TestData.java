package com.thoughtworks.blipit;

import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.GeoPt;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.blipit.domain.Channel;

public class TestData {

    static class Channels {
        private static Category category = Utils.convert(com.thoughtworks.contract.common.Category.AD);
        public static final Channel MOVIE = new Channel(null, "Movies", category);
        public static final Channel FOOD = new Channel(null, "Food", category);
    }

    //All the Geo points have the real co-ordinates, please keep it that way if you add more locations.
    static class Alerts {
        public static final Blip NAVARANG = new Blip("Navarang", "Kannada Movie", new GeoPt(12.99800466383297f, 77.55291799658964f));
        public static final Blip FAMELIDO = new Blip("Fame Lido", "Movie premier", new GeoPt(12.973881024116972f, 77.62282172793577f));
        public static final Blip PVR = new Blip("PVR", "Movie screening", new GeoPt(12.935131942393395f, 77.61056939715574f));
        public static final Blip MTR = new Blip("MTR", "Karnataka ethnic food", new GeoPt(12.955009113220616f, 77.58542100543211f));
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
