package com.thoughtworks.blipit;

import com.thoughtworks.blipit.domain.Alert;
import com.thoughtworks.blipit.persistance.DataStoreHelper;
import com.thoughtworks.contract.BlipItRequest;
import com.thoughtworks.contract.BlipItResponse;
import com.thoughtworks.contract.BlipItSubscribeResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;
import java.util.logging.Logger;

public class BlipItSubscribeResourceImpl extends ServerResource implements BlipItSubscribeResource {
    private static final Logger log = Logger.getLogger(BlipItSubscribeResourceImpl.class.getName());

    @Get
    public String showMessage() {
        return "Hi there ! You've reached the BlipIt server !! I can only process HTTP post requests !!!";
    }

    @Post
    public BlipItResponse getBlips(BlipItRequest blipItRequest) {
        BlipItResponse blipItResponse = new BlipItResponse();
        Query query = null;
        try {
            PersistenceManager persistenceManager = DataStoreHelper.getPersistenceManager();
            query = persistenceManager.newQuery(Alert.class);
            List<Alert> alerts = (List<Alert>) query.execute();
            if (Utils.isNotEmpty(alerts)) {
                for (Alert alert : alerts) {
                    blipItResponse.addBlips(alert.toBlip());
                }
            } else {
                blipItResponse.setBlipItError(Utils.noBlipsFoundError());
            }
        } finally {
            if (query != null) {
                query.closeAll();
            }
        }

        return blipItResponse;
    }
}
