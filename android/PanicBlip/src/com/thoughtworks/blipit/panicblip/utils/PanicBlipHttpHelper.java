package com.thoughtworks.blipit.panicblip.utils;

import android.util.Log;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.APP_TAG;

public class PanicBlipHttpHelper {
    private static PanicBlipHttpHelper instance = new PanicBlipHttpHelper();
    private static String USER_AGENT = "BlipIt/1.0";
    private static String CONTENT_TYPE = "application/json";
    private static final int BUFFER_SIZE = 8 * 1024;
    private final HttpClient httpClient;
    private final Gson gson;

    public static PanicBlipHttpHelper getInstance() {
        return instance;
    }

    private PanicBlipHttpHelper() {
        HttpParams params = new BasicHttpParams();

        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUserAgent(params, USER_AGENT);

        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        HttpClientParams.setRedirecting(params, false);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);
        httpClient = new DefaultHttpClient(manager, params);
        gson = new Gson();
    }

    public String getAllChannelsAsJson(String blipItSvcHost) {
        HttpEntity httpEntity = null;
        String channelsJson = null;
        try {
            String blipItSvcUrl = String.format("http://%s/blipit/panic/channel", blipItSvcHost);
            HttpGet httpGet = new HttpGet(blipItSvcUrl);
            httpGet.addHeader("Content-Type", CONTENT_TYPE);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            InputStream content = httpEntity.getContent();
            channelsJson = convertStreamToString(content);
        } catch (IOException e) {
            Log.e(APP_TAG, e.getMessage(), e);
        } finally {
            if (httpEntity != null) {
                try {
                    httpEntity.consumeContent();
                } catch (IOException e) {
                    Log.e(APP_TAG, e.getMessage(), e);
                }
            }
        }
        return channelsJson;
    }

    private String convertStreamToString(InputStream strStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(strStream), BUFFER_SIZE);
        StringBuilder buffer = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
        } catch (IOException e) {
            Log.e(APP_TAG, e.getMessage(), e);
        } finally {
            try {
                strStream.close();
            } catch (IOException e) {
                Log.e(APP_TAG, e.getMessage(), e);
            }
        }

        return buffer.toString();
    }
}
