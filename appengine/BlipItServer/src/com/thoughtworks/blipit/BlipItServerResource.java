package com.thoughtworks.blipit;

import com.thoughtworks.blipit.domain.Alert;
import com.thoughtworks.blipit.persistance.Persist;
import com.thoughtworks.contract.BlipItRequest;
import com.thoughtworks.contract.BlipItResource;
import com.thoughtworks.contract.BlipItResponse;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import javax.jdo.PersistenceManager;
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
//        PersistAndRetrieveAlert();
        return blipItResponse;
    }

    private void PersistAndRetrieveAlert() {
        PersistenceManager manager = null;
        try {
            manager = Persist.getPersistenceManager();
            Alert alert = new Alert("Wildcraft", "10% off on trekking bags :)");
            manager.makePersistent(alert);
            Alert retrievedAlert = manager.getObjectById(Alert.class, alert.getKey());
            if (alert.getSource().equals(retrievedAlert.getSource()) && alert.getDescription().equalsIgnoreCase(retrievedAlert.getDescription()))
                log.info("Alert persisted and retrieved successfully");
            else
                log.severe("Something went wrong with persistence. Retrieved alert is :" + retrievedAlert.getSource() + " " + retrievedAlert.getDescription());
        } catch (Exception e) {
            log.severe("Persistance of Alert failed" + "\r\n" + e);
        } finally {
            if(manager != null)
                manager.close();
        }
    }
}
