/*
 * Copyright (c) 2010 BlipIt Committers
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

package com.thoughtworks.blipit;

import com.google.appengine.api.datastore.GeoPt;
import com.thoughtworks.blipit.domain.Alert;
import com.thoughtworks.blipit.persistance.DataStoreHelper;
import com.thoughtworks.contract.BlipItResponse;
import com.thoughtworks.contract.GeoLocation;
import com.thoughtworks.contract.common.Channel;
import com.thoughtworks.contract.common.ChannelCategory;
import com.thoughtworks.contract.common.GetChannelsResponse;
import com.thoughtworks.contract.publish.BlipItPublishResource;
import com.thoughtworks.contract.publish.DeleteBlipRequest;
import com.thoughtworks.contract.publish.SaveBlipRequest;
import com.thoughtworks.contract.publish.SaveBlipResponse;
import com.thoughtworks.contract.utils.ChannelUtils;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.thoughtworks.blipit.Utils.splitByComma;

public class BlipItPublishResourceImpl extends BlipItCommonServerResource implements BlipItPublishResource {
    private static final Logger log = Logger.getLogger(BlipItPublishResourceImpl.class.getName());

    public BlipItPublishResourceImpl() {
        super();
    }

    @Post
    public Representation acceptAlert(Representation entity) {
        final Representation[] result = {null};
        Alert alert = getAlert(new Form(entity));
        DataStoreHelper.save(alert, new Utils.ResultHandler<Alert>() {
            public void onSuccess(Alert arg) {
                setStatus(Status.SUCCESS_CREATED);
                result[0] = new StringRepresentation("Alert creation successful", MediaType.TEXT_PLAIN);
                log.info("Alert with title, " + arg.getSource() + " saved successfully with ID: " + arg.getKey());
            }

            public void onError(Throwable throwable) {
                setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                result[0] = new StringRepresentation("Alert creation failed with error: " + throwable, MediaType.TEXT_PLAIN);
                log.log(Level.SEVERE, "Alert creation failed with error", throwable);
            }
        });
        return result[0];
    }

    @Post
    public SaveBlipResponse saveBlip(SaveBlipRequest saveBlipRequest) {
        Alert alert = getAlert(saveBlipRequest);
        final SaveBlipResponse saveBlipResponse = new SaveBlipResponse();
        DataStoreHelper.save(alert, new Utils.ResultHandler<Alert>() {
            public void onSuccess(Alert savedAlert) {
                saveBlipResponse.setSuccess();
                String keyAsString = savedAlert.getKeyAsString();
                saveBlipResponse.setBlipId(keyAsString);
                log.info("Alert with title, " + savedAlert.getSource() + " saved successfully with ID: " + keyAsString);
            }

            public void onError(Throwable throwable) {
                saveBlipResponse.setFailure(Utils.getBlipItError(throwable.getMessage()));
                log.log(Level.SEVERE, "Alert creation failed with error", throwable);
            }
        });
        return saveBlipResponse;
    }

    @Delete
    public BlipItResponse deleteBlip(DeleteBlipRequest deleteBlipRequest) {
        final String blipId = deleteBlipRequest.getBlipId();
        final BlipItResponse blipItResponse = new BlipItResponse();
        DataStoreHelper.delete(Alert.class, blipId, new Utils.ResultHandler<Alert>() {
            public void onSuccess(Alert arg) {
                blipItResponse.setSuccess();
                log.info("Alert with ID, " + blipId + " deleted successfully");
            }

            public void onError(Throwable throwable) {
                blipItResponse.setFailure(Utils.getBlipItError(throwable.getMessage()));
                log.log(Level.SEVERE, "Alert deletion failed with error", throwable);
            }
        });
        return blipItResponse;
    }

    // TODO: Take the alert title & desc from PanicBlip.
    private Alert getAlert(SaveBlipRequest saveBlipRequest) {
        GeoLocation blipLocation = saveBlipRequest.getBlipLocation();
        // TODO: Load & link the channels from data store
        List<Channel> applicableChannels = saveBlipRequest.getApplicableChannels();
        return new Alert("Panic Title", "Panic Desc", Utils.asGeoPoint(blipLocation), ChannelUtils.toChannelNames(applicableChannels));
    }

    private Alert getAlert(Form form) {
        String alertTitle = form.getFirstValue("alert.title");
        String alertDescription = form.getFirstValue("alert.desc");
        Float alertLatitude = Float.valueOf(form.getFirstValue("alert.loc.lat"));
        Float alertLongitude = Float.valueOf(form.getFirstValue("alert.loc.long"));
        List<String> channels = splitByComma(form.getFirstValue("alert.channels"));
        return new Alert(alertTitle, alertDescription, new GeoPt(alertLatitude, alertLongitude), channels);
    }

    @Get
    public GetChannelsResponse getAvailableChannels(ChannelCategory channelCategory) {
        return getChannels(channelCategory);
    }
}
