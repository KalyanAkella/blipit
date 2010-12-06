package com.thoughtworks.blipit;

import com.google.appengine.api.datastore.GeoPt;
import com.thoughtworks.blipit.domain.Alert;
import com.thoughtworks.blipit.persistance.DataStoreHelper;
import com.thoughtworks.contract.BlipItPublishResource;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.thoughtworks.blipit.Utils.splitByComma;

public class BlipItPublishResourceImpl extends ServerResource implements BlipItPublishResource {
    private static final Logger log = Logger.getLogger(BlipItPublishResourceImpl.class.getName());

    @Post
    public Representation acceptAlert(Representation entity) {
        Representation result = null;

        try {
            Alert alert = getAlert(new Form(entity));
            alert = DataStoreHelper.save(alert);
            setStatus(Status.SUCCESS_CREATED);
            result = new StringRepresentation("Alert creation successful", MediaType.TEXT_PLAIN);
            log.info("Alert with title, " + alert.getSource() + " saved successfully with ID: " + alert.getKey());
        } catch (Exception e) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            result = new StringRepresentation("Alert creation failed with error: " + e, MediaType.TEXT_PLAIN);
            log.log(Level.SEVERE, "Alert creation failed with error", e);
        }
        return result;
    }

    private Alert getAlert(Form form) {
        String alertTitle = form.getFirstValue("alert.title");
        String alertDescription = form.getFirstValue("alert.desc");
        Float alertLatitude = Float.valueOf(form.getFirstValue("alert.loc.lat"));
        Float alertLongitude = Float.valueOf(form.getFirstValue("alert.loc.long"));
        List<String> channels = splitByComma(form.getFirstValue("alert.channels"));
        return new Alert(alertTitle, alertDescription, new GeoPt(alertLatitude, alertLongitude), channels);
    }

}
