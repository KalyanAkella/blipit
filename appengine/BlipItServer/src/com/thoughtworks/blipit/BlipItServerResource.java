package com.thoughtworks.blipit;

import com.thoughtworks.blipit.domain.Add;
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
//        PersistAndRetrieveAdd();
        return blipItResponse;
    }

    private void PersistAndRetrieveAdd() {
        PersistenceManager manager = null;
        try {
            manager = Persist.getPersistenceManager();
            Add add = new Add("Wildcraft", "10% off on trekking bags :)");
            manager.makePersistent(add);
            Add retrievedAdd = manager.getObjectById(Add.class, add.getKey());
            if (add.getSource().equals(retrievedAdd.getSource()) && add.getDescription().equalsIgnoreCase(retrievedAdd.getDescription()))
                log.info("Add persisted and retrieved successfully");
            else
                log.severe("Something went wrong with persistence. Retrieved add is :" + retrievedAdd.getSource() + " " + retrievedAdd.getDescription());
        } catch (Exception e) {
            log.severe("Persistance of Add failed" + "\r\n" + e);
        } finally {
            if(manager != null)
                manager.close();
        }
    }
}
