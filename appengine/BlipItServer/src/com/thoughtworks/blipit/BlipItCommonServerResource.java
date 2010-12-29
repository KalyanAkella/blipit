package com.thoughtworks.blipit;

import com.thoughtworks.blipit.domain.Channel;
import com.thoughtworks.contract.common.Category;
import com.thoughtworks.blipit.persistance.BlipItRepository;
import com.thoughtworks.contract.common.GetChannelsResponse;
import org.restlet.resource.ServerResource;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BlipItCommonServerResource extends ServerResource {
    private static final Logger log = Logger.getLogger(BlipItCommonServerResource.class.getName());
    protected BlipItRepository blipItRepository;

    public BlipItCommonServerResource() {
        blipItRepository = new BlipItRepository();
    }

    protected GetChannelsResponse getChannels(final Category channelCategory) {
        final GetChannelsResponse channelsResponse = new GetChannelsResponse();
        com.thoughtworks.blipit.domain.Category category = com.thoughtworks.blipit.domain.Category.convert(channelCategory);
        blipItRepository.retrieveChannelsByCategory(category, new Utils.ResultHandler<Channel>() {
            public void onSuccess(Channel savedChannel) {
                channelsResponse.setSuccess();
                channelsResponse.addChannel(savedChannel.getKeyAsString(), savedChannel.getName(), channelCategory);
            }

            public void onError(Throwable throwable) {
                log.log(Level.SEVERE, "An error occurred while fetching channels", throwable);
                channelsResponse.setFailure(Utils.getBlipItError(throwable.getMessage()));
            }
        });
        return channelsResponse;
    }
}
