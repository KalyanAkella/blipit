package com.thoughtworks.blipit;

import com.thoughtworks.blipit.domain.Channel;
import com.thoughtworks.blipit.domain.ChannelCategory;
import com.thoughtworks.blipit.persistance.BlipItRepository;
import com.thoughtworks.contract.common.BlipItCommonResource;
import com.thoughtworks.contract.common.GetChannelsResponse;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BlipItCommonResourceImpl extends ServerResource implements BlipItCommonResource {
    private static final Logger log = Logger.getLogger(BlipItCommonResourceImpl.class.getName());
    private BlipItRepository blipItRepository;

    public BlipItCommonResourceImpl() {
        blipItRepository = new BlipItRepository();
    }

    @Post
    public GetChannelsResponse getPanicChannels() {
        return getChannels(ChannelCategory.PANIC);
    }

    @Post
    public GetChannelsResponse getFreeChannels() {
        return getChannels(ChannelCategory.AD);
    }

    private GetChannelsResponse getChannels(ChannelCategory channelCategory) {
        final GetChannelsResponse channelsResponse = new GetChannelsResponse();
        blipItRepository.retrieveChannelsByCategory(channelCategory, new Utils.ResultHandler<Channel>() {
            public void onSuccess(Channel savedChannel) {
                channelsResponse.addChannel(savedChannel.getKeyAsString(), savedChannel.getName(), savedChannel.getDescription());
            }

            public void onError(Throwable throwable) {
                log.log(Level.SEVERE, "An error occurred while fetching channels", throwable);
                channelsResponse.setFailure(Utils.getBlipItError(throwable.getMessage()));
            }
        });
        return channelsResponse;
    }
}
