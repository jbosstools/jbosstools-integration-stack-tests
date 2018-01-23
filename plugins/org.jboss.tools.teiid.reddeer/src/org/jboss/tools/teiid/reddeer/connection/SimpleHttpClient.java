package org.jboss.tools.teiid.reddeer.connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.reddeer.common.logging.Logger;

/**
 * Change simple http client implementation to use apache http client
 * 
 * @author mkralik
 *
 */
public class SimpleHttpClient {

    private static Logger log = Logger.getLogger(SimpleHttpClient.class);

	private Map<String, String> headers = new HashMap<String, String>();
    private CloseableHttpClient httpclient = null;

    public SimpleHttpClient() {
        httpclient = HttpClients.createDefault();
	}

    public SimpleHttpClient(String userName, String password) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
        provider.setCredentials(AuthScope.ANY, credentials);

        httpclient = HttpClients.custom().setDefaultCredentialsProvider(provider).build();
    }

    public SimpleHttpClient addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public String get(String url) throws IOException {
        log.info("GET method on url: " + url);
        HttpGet request = new HttpGet(url);
        request.setConfig(RequestConfig.copy(RequestConfig.DEFAULT).setSocketTimeout(5000).setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000).build());
        String result = null;
        for (Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }
        try (CloseableHttpResponse httpResponse = httpclient.execute(request)) {
            result = EntityUtils.toString(httpResponse.getEntity());
            log.info("Result GET method is:" + result);
        } finally {
            headers.clear();
        }
        return result;
	}

    public String post(String url, String data) throws IOException {
        log.info("POST method on url: '" + url + "' with data: " + data);
        HttpPost request = new HttpPost(url);
        request.setConfig(RequestConfig.copy(RequestConfig.DEFAULT).setSocketTimeout(5000).setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000).build());
        String result = null;
        for (Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }
        StringEntity params = new StringEntity(data);
        request.setEntity(params);
        try (CloseableHttpResponse httpResponse = httpclient.execute(request)) {
            result = EntityUtils.toString(httpResponse.getEntity());
            log.info("Result POST method is:" + result);
        } finally {
            headers.clear();
        }
        return result;
	}
}