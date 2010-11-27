package com.thoughtworks.blipit;

import com.thoughtworks.contract.BlipItRequest;
import com.thoughtworks.contract.BlipItResource;
import com.thoughtworks.contract.BlipItResponse;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.logging.Logger;

public class BlipItServerResource extends ServerResource implements BlipItResource {
    private static final Logger log = Logger.getLogger(BlipItServerResource.class.getName());

    @Get
    public String showMessage() {
        return "Hi there ! You've reached the BlipIt server !! I can only process HTTP post requests !!!";
    }

    @Post
    public BlipItResponse echo(BlipItRequest blipItRequest) {
        log.info("Blip Request received. Message: " + blipItRequest.getMessage());
        BlipItResponse blipItResponse = new BlipItResponse();
        blipItResponse.setMessage(blipItRequest.getMessage());
        log.info("Blip Response sent. Message: " + blipItResponse.getMessage());
        return blipItResponse;
    }
}
