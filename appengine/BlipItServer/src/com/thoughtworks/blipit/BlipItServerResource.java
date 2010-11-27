package com.thoughtworks.blipit;

import com.thoughtworks.contract.BlipItRequest;
import com.thoughtworks.contract.BlipItResource;
import com.thoughtworks.contract.BlipItResponse;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class BlipItServerResource extends ServerResource implements BlipItResource {

    @Get
    public String showMessage() {
        return "Hi there ! You've reached the BlipIt server !! I can only process HTTP post requests !!!";
    }

    @Post
    public BlipItResponse echo(BlipItRequest blipItRequest) {
        BlipItResponse blipItResponse = new BlipItResponse();
        blipItResponse.setMessage(blipItRequest.getMessage());
        return blipItResponse;
    }
}
