package com.thoughtworks.blipit.utils;

import com.thoughtworks.contract.BlipItRequest;
import com.thoughtworks.contract.BlipItResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class HttpHelper {
    private static final HttpClient httpClient;

    private static final String USER_AGENT = "BlipIt/1.0";
    private static final String CONTENT_TYPE = "binary/octet-stream";
    private static final String BLIPIT_SERVICE_URI = "http://10.0.2.2:8080/blipit";

    static {
        final HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");

        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        HttpClientParams.setRedirecting(params, false);

        HttpProtocolParams.setUserAgent(params, USER_AGENT);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);
        httpClient = new DefaultHttpClient(manager, params);
    }

    private HttpHelper() {
    }

    public static BlipItResponse getResponse(BlipItRequest blipItRequest) throws IOException, ClassNotFoundException {
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(convertToBytes(blipItRequest));
        byteArrayEntity.setContentType(CONTENT_TYPE);
        byteArrayEntity.setChunked(true);

        HttpPost httpPost = new HttpPost(BLIPIT_SERVICE_URI);
        httpPost.setEntity(byteArrayEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream inputStream = httpEntity.getContent();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        BlipItResponse blipItResponse = (BlipItResponse) objectInputStream.readObject();
        httpEntity.consumeContent();
        return blipItResponse;
    }

    private static byte[] convertToBytes(BlipItRequest blipItRequest) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(blipItRequest);
        return byteArrayOutputStream.toByteArray();
    }

}
