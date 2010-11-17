package com.thoughtworks.blipit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class TransportLayerImpl implements TransportLayer {

    private HttpClient httpClient;

    private ResponseHandler<String> responseHandler;

    public TransportLayerImpl(){
        httpClient = new DefaultHttpClient();
        responseHandler = new BasicResponseHandler();
    }

    public String getBlipTitle() throws IOException {
        HttpGet get = new HttpGet("http://blipitserver.appspot.com/title");
        return httpClient.execute(get, responseHandler);
    }

    public String getBlipSnippet() throws IOException {
        HttpGet get = new HttpGet("http://blipitserver.appspot.com/snippet");
        return httpClient.execute(get, responseHandler);
    }
}
