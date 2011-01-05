package com.thoughtworks.blipit.restful;

import com.google.gson.Gson;
import com.thoughtworks.blipit.Utils;
import com.thoughtworks.blipit.domain.Channel;
import com.thoughtworks.blipit.persistence.BlipItRepository;
import com.thoughtworks.contract.common.Category;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.List;

public class ChannelsResource extends ServerResource {
    private String category;
    private BlipItRepository blipItRepository;

    @Override
    protected void doInit() throws ResourceException {
        this.getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        this.category = (String) getRequest().getAttributes().get("category");
        blipItRepository = new BlipItRepository();
    }

    // TODO: Send ErrorRepresentation in case of errors
    @Override
    protected Representation get(Variant variant) throws ResourceException {
        Category channelCategory = Category.valueOf(category.toUpperCase());
        List<Channel> channels = blipItRepository.retrieveChannelsByCategory(Utils.convert(channelCategory));
        String json = new Gson().toJson(channels);
        if (variant.getMediaType().equals(MediaType.APPLICATION_JSON))
            return new JsonRepresentation(json);
        return new StringRepresentation(json);
    }
}