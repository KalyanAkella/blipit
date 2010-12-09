package com.thoughtworks.blipit;

import com.thoughtworks.blipit.domain.Alert;
import com.thoughtworks.blipit.persistance.DataStoreHelper;
import com.thoughtworks.contract.BlipItRequest;
import com.thoughtworks.contract.BlipItResponse;
import com.thoughtworks.contract.BlipItSubscribeResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.logging.Logger;

public class BlipItSubscribeResourceImpl extends ServerResource implements BlipItSubscribeResource {
    private static final Logger log = Logger.getLogger(BlipItSubscribeResourceImpl.class.getName());

    @Get
    public String showMessage() {
        return "Hi there ! You've reached the BlipIt server !! I can only process HTTP post requests !!!";
    }

    @Post
    public BlipItResponse getBlips(BlipItRequest blipItRequest) {
        final BlipItResponse blipItResponse = new BlipItResponse();
        DataStoreHelper.retrieveAllAndProcess(Alert.class,
                new Utils.Task<Alert>() {
                    public void perform(Alert arg) {
                        blipItResponse.addBlips(arg.toBlip());
                    }
                }, new Utils.Task<Throwable>() {
                    public void perform(Throwable arg) {
                        blipItResponse.setBlipItError(Utils.getBlipItError(arg.getMessage()));
                    }
                });
        return blipItResponse;
    }
}
