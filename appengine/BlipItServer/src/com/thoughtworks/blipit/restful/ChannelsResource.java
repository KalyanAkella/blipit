package com.thoughtworks.blipit.restful;

import com.google.gson.Gson;
import com.thoughtworks.blipit.Utils;
import com.thoughtworks.blipit.domain.Channel;
import com.thoughtworks.blipit.persistence.BlipItRepository;
import com.thoughtworks.blipit.domain.CategoryEnum;
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
    protected Gson gson;

    @Override
    protected void doInit() throws ResourceException {
        this.getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        this.category = (String) getRequestAttributes().get("category");
        this.blipItRepository = new BlipItRepository();
        this.gson = new Gson();
    }

    // TODO: Send ErrorRepresentation in case of errors
    @Override
    protected Representation get(Variant variant) throws ResourceException {
        CategoryEnum channelCategory = CategoryEnum.valueOf(category.toUpperCase());
        List<Channel> channels = blipItRepository.retrieveChannelsByCategory(Utils.convert(channelCategory));
        String json = gson.toJson(channels);
        if (variant.getMediaType().equals(MediaType.APPLICATION_JSON))
            return new JsonRepresentation(json);
        return new StringRepresentation(json);
    }
}
