package com.thoughtworks.blipit.utils;

import android.util.Log;
import com.thoughtworks.blipit.types.Blip;
import com.thoughtworks.blipit.types.Filter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
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
import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.thoughtworks.blipit.utils.BlipItUtils.APP_TAG;

public class BlipItHttpHelper {
    private static BlipItHttpHelper instance = new BlipItHttpHelper();
    private static String USER_AGENT = "BlipIt/1.0";
    private static String JSON_CONTENT_TYPE = "application/json";
    private static final int BUFFER_SIZE = 8 * 1024;
    private final HttpClient httpClient;

    public static BlipItHttpHelper getInstance() {
        return instance;
    }

    private BlipItHttpHelper() {
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
    }

    public String getAllChannelsAsJson(String blipItSvcHost) {
        HttpEntity httpEntity = null;
        String channelsJson = null;
        try {
            String blipItSvcUrl = String.format("http://%s/blipit/ad/channel", blipItSvcHost);
            HttpGet httpGet = new HttpGet(blipItSvcUrl);
            httpGet.addHeader("Content-Type", JSON_CONTENT_TYPE);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            InputStream content = httpEntity.getContent();
            channelsJson = convertStreamToString(content);
        } catch (IOException e) {
            logError(e);
        } finally {
            closeEntity(httpEntity);
        }
        return channelsJson;
    }

    public String getAdsAsJson(String blipItSvcHost, String filterId) {
        HttpEntity httpEntity = null;
        String adsJson = null;
        try {
            String blipItSvcUrl = String.format("http://%s/blipit/ad/filter/%s", blipItSvcHost, filterId);
            HttpGet httpGet = new HttpGet(blipItSvcUrl);
            httpGet.addHeader("Content-Type", JSON_CONTENT_TYPE);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            InputStream content = httpEntity.getContent();
            adsJson = convertStreamToString(content);
        } catch (IOException e) {
            logError(e);
        } finally {
            closeEntity(httpEntity);
        }
        return adsJson;
    }

    public List<Blip> filter(String blipItSvcHost, Filter filter) {
        List<Blip> ads = null;
        Filter savedFilter = saveFilter(blipItSvcHost, filter);
        if (savedFilter != null) {
            String adsAsJson = getAdsAsJson(blipItSvcHost, savedFilter.getKey().getId());
            if (adsAsJson != null) ads = BlipItUtils.toAds(adsAsJson);
        }
        return ads;
    }

    public Filter saveFilter(String blipItSvcHost, Filter filter) {
        Filter savedFilter = null;
        HttpEntity httpEntity = null;
        try {
            String filterJson = BlipItUtils.toFilterJson(filter);
            String blipItSvcUrl = String.format("http://%s/blipit/ad/filter", blipItSvcHost);
            StringEntity stringEntity = new StringEntity(filterJson, "UTF-8");
            stringEntity.setContentType(JSON_CONTENT_TYPE);
            HttpPost httpPost = new HttpPost(blipItSvcUrl);
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            InputStream content = httpEntity.getContent();
            savedFilter = BlipItUtils.toFilter(convertStreamToString(content));
        } catch (UnsupportedEncodingException e) {
            logError(e);
        } catch (ClientProtocolException e) {
            logError(e);
        } catch (IOException e) {
            logError(e);
        } finally {
            closeEntity(httpEntity);
        }
        return savedFilter;
    }

    private void closeEntity(HttpEntity httpEntity) {
        if (httpEntity != null) {
            try {
                httpEntity.consumeContent();
            } catch (IOException e) {
                logError(e);
            }
        }
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
            logError(e);
        } finally {
            try {
                strStream.close();
            } catch (IOException e) {
                logError(e);
            }
        }
        String response = buffer.toString();
        Log.i(APP_TAG, response);
        return response;
    }

    private void logError(Exception e) {
        Log.e(APP_TAG, e.getMessage(), e);
    }
}
