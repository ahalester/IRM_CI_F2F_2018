package de.aqua.web.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class RESTClientUtil {

    private static final String BASIC_AUTH = "Basic ";
    public static Logger LOG = LoggerFactory.getLogger(RESTClientUtil.class);

    // trusting all certificates
    public static void doTrustToCertificates() {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                return;
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                return;
            }
        }};

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    LOG.info("Warning: URL host '" + urlHostName + "' is different to SSLSession host '"
                            + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    public static void httpHeaderAuthenticator(final String username, final String password) {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
    }

    public static void apiCall(String serverRootUri, String callType, String jsonBody,
                               Map<String, String> callResponse) {
        try {
            Client client = new Client();
            client = Client.create();

            WebResource webResource = client.resource(serverRootUri);
            String input = jsonBody;

            ClientResponse response = null;

            switch (callType) {
                case "GET": {
                    response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
                            .get(ClientResponse.class);
                }
                break;
                case "POST": {
                    response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
                            .post(ClientResponse.class, input);
                }
                break;
                case "PUT": {
                    response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
                            .put(ClientResponse.class, input);
                }
                break;
                case "DELETE": {
                    response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
                            .delete(ClientResponse.class, input);
                }
                break;
            }

            LOG.info("###########################");
            LOG.info("\nOutput from Server...\n");

            String responseStatus = String.valueOf(response.getStatus());
            String responseHeader = response.getHeaders().toString();
            String responseBody = response.getEntity(String.class);

            LOG.info(responseStatus + "\r\n\r\n" + responseHeader + "\r\n\r\n" + responseBody);

            callResponse.put("ResponseCode", responseStatus);
            callResponse.put("ResponseHeader", responseHeader);
            callResponse.put("ResponseBody", responseBody);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Call failed!");
        }
    }

    public static void apiCall(String serverRootUri, List<String> proxy, String callType, String jsonBody,
                               int responseCode) {
        apiCall(serverRootUri, callType, jsonBody, null);
    }

    public static URLConnection setUsernamePassword(URL url, String username, String pasword) throws IOException {
        URLConnection urlConnection = url.openConnection();
        String authString = username + ":" + pasword;
        String authStringEnc = new String(Base64.encodeBase64(authString.getBytes()));
        urlConnection.setRequestProperty("Authorization", BASIC_AUTH + authStringEnc);
        return urlConnection;
    }
}
