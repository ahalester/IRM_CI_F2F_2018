package de.aqua.web.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import static de.aqua.web.utils.PropertiesFileUtil.getProperty;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class APICallUtil {

    private static Logger LOG = LoggerFactory.getLogger(APICallUtil.class);
    private static String authType = "";
    private static final String AUTH_TYPE = "Basic ";
    private static final String AUTHORIZATION = "Authorization";

    /**
     * Returns a SSL connection client that uses or not HTTP header login and proxy settings.
     *
     * @param url          an absolute URL giving the API path
     * @param httpHeader   list that contains or not the HTTP header login credentials
     * @param proxy        list that contains or not the proxy host and port
     * @param callType     string that defines the requested API call (GET, POST, PUT, DELETE)
     * @param body         string that contains the API call message
     * @param callResponse dictionary containing the API call response (ResponseCode, ResponseHeader,
     *                     ResponseBody)
     * @return SSL connection client
     */
    public static ClientResponse callRequest(String url, List<String> httpHeader,
                                             List<String> proxy, String callType, String body,
                                             Map<String, String> callResponse) {

        String acctUser;
        String acctPassword;
        WebResource webResource;
        String authString = null;
        Client client = null;

        if (httpHeader.size() > 1 && !httpHeader.get(0).equalsIgnoreCase("token")) {
            acctUser = httpHeader.get(0);
            acctPassword = httpHeader.get(1);
            authString = getAuthHeaderValue(acctUser, acctPassword);
            authType = "basic";
        } else if (httpHeader.get(0).equalsIgnoreCase("token")) {
            authString = getProperty("temp", "token");
            authType = "token";
        }

        url = urlStringValidation(url);
        LOG.info("\n*******************");

        try {
            client = SSLUtil.getSSLClient(proxy);

            webResource = client.resource(url);
            LOG.debug(callType + " Request " + webResource);
            ClientResponse response = null;

            switch (callType.toUpperCase()) {
                case "GET": {
                    response = getRequest(authType, webResource, authString);
                    break;
                }
                case "POST": {
                    response = postRequest(body, authType, webResource, authString);
                    break;
                }
                case "PUT": {
                    response = putRequest(body, authType, webResource, authString);
                    break;
                }
                case "DELETE": {
                    response = deleteRequest(body, authType, webResource, authString);
                    break;
                }
                default: {
                    break;
                }
            }

            LOG.info("\nOutput from server...\n");

            String responseStatus = String.valueOf(response.getStatus());
            String responseHeader = response.getHeaders().toString();
            String responseBody = null;
            if (!(callType.equalsIgnoreCase("DELETE") && responseStatus.equals("204"))) {
                responseBody = response.getEntity(String.class);
            }

            LOG.info(responseStatus + "\r\n\r\n" + responseHeader + "\r\n\r\n"
                    + responseBody);

            callResponse.put("ResponseCode", responseStatus);
            // callResponse.put("ResponseHeader", responseHeader);
            callResponse.put("ResponseBody", responseBody);

            return response;
        } catch (IllegalArgumentException | KeyManagementException | NoSuchAlgorithmException
                | KeyStoreException | CertificateException | IOException e) {
            LOG.error("Exception occurred", e);
            return null;
        } finally {
            client.destroy();
            LOG.debug("Resource assigned null");
            //  webResource = null;
        }
    }

    /**
     * Returns the response of the API GET call.
     *
     * @param authType    flag that determines the header authentication type
     * @param webResource is an instance of Client
     * @param authString  string which will be passed as a parameter to the web resource for HTTP
     *                    header login
     * @return API GET response
     */
    @GET
    private static ClientResponse getRequest(String authType, WebResource webResource,
                                             String authString) {
        ClientResponse response;
        switch (authType) {
            case "basic": {
                response = webResource.accept(MediaType.APPLICATION_JSON)
                        .type(MediaType.APPLICATION_JSON).header(AUTHORIZATION,
                                AUTH_TYPE + authString).get(ClientResponse.class);
                break;
            }
            case "token": {
                response = webResource.accept(MediaType.APPLICATION_JSON)
                        .type(MediaType.APPLICATION_JSON).header(AUTHORIZATION, authString)
                        .get(ClientResponse.class);
                break;
            }
            default: {
                response = webResource.accept(MediaType.APPLICATION_JSON)
                        .type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
                break;
            }
        }
        return response;
    }

    /**
     * Returns the response of the API POST call.
     *
     * @param body        string containing the specific API call message
     * @param authType    flag that determines the header authentication type
     * @param webResource is an instance of Client
     * @param authString  string which will be passed as a parameter to the web resource for HTTP
     *                    header login
     * @return API POST response
     */
    @POST
    private static ClientResponse postRequest(String body, String authType,
                                              WebResource webResource, String authString) {
        ClientResponse response;
        switch (authType) {
            case "basic": {
                response = webResource.accept(MediaType.APPLICATION_JSON)
                        .type(MediaType.APPLICATION_JSON).header(AUTHORIZATION,
                                AUTH_TYPE + authString).post(ClientResponse.class, body);
                break;
            }
            case "token": {
                response = webResource.accept(MediaType.APPLICATION_JSON)
                        .type(MediaType.APPLICATION_JSON).header(AUTHORIZATION, authString)
                        .post(ClientResponse.class, body);
                break;
            }
            default: {
                response = webResource.accept(MediaType.APPLICATION_JSON)
                        .type(MediaType.APPLICATION_JSON).post(ClientResponse.class, body);
                break;
            }
        }
        return response;
    }

    /**
     * Returns the response of the API PUT call.
     *
     * @param body        string containing the specific API call message
     * @param authType    flag that determines the header authentication type
     * @param webResource is an instance of Client
     * @param authString  string which will be passed as a parameter to the web resource for HTTP
     *                    header login
     * @return API PUT response
     */
    @PUT
    private static ClientResponse putRequest(String body, String authType, WebResource webResource,
                                             String authString) {
        ClientResponse response;
        switch (authType) {
            case "basic": {
                response = webResource.accept(MediaType.APPLICATION_JSON)
                        .type(MediaType.APPLICATION_JSON).header(AUTHORIZATION,
                                AUTH_TYPE + authString).put(ClientResponse.class, body);
                break;
            }
            case "token": {
                response = webResource.accept(MediaType.APPLICATION_JSON)
                        .type(MediaType.APPLICATION_JSON).header(AUTHORIZATION, authString)
                        .put(ClientResponse.class, body);
                break;
            }
            default: {
                response = webResource.accept(MediaType.APPLICATION_JSON)
                        .type(MediaType.APPLICATION_JSON).put(ClientResponse.class, body);
                break;
            }
        }
        return response;
    }

    /**
     * Returns the response of the API DELETE call.
     *
     * @param body        string containing the specific API call message
     * @param authType    flag that determines the header authentication type
     * @param webResource is an instance of Client
     * @param authString  string which will be passed as a parameter to the web resource for HTTP
     *                    header login
     * @return API DELETE response
     */
    @DELETE
    private static ClientResponse deleteRequest(String body, String authType,
                                                WebResource webResource, String authString) {
        ClientResponse response;
        switch (authType) {
            case "basic": {
                response = webResource.accept(MediaType.APPLICATION_JSON)
                        .type(MediaType.APPLICATION_JSON).header(AUTHORIZATION,
                                AUTH_TYPE + authString).delete(ClientResponse.class, body);
                break;
            }
            case "token": {
                response = webResource.accept(MediaType.APPLICATION_JSON)
                        .type(MediaType.APPLICATION_JSON).header(AUTHORIZATION, authString)
                        .delete(ClientResponse.class, body);
                break;
            }
            default: {
                response = webResource.accept(MediaType.APPLICATION_JSON)
                        .type(MediaType.APPLICATION_JSON).delete(ClientResponse.class, body);
                break;
            }
        }
        return response;
    }

    /**
     * Returns the string containing the authentication credentials for HTTP header login.
     *
     * @param acctUser     authentication user name
     * @param acctPassword authentication password
     * @return encoded authentication string
     */
    private static String getAuthHeaderValue(String acctUser, String acctPassword) {
        return new String(Base64.encode((acctUser + ":" + acctPassword).getBytes()));
    }

    /**
     * Returns the absolute URL without un-encoded chars.
     *
     * @param url string containing the raw URL
     * @return absolute URL
     */
    private static String urlStringValidation(String url) {
        if (null != url) {
            if (url.trim().contains(" ")) {
                url = url.replace(" ", "%20");
            }
        }
        return url;
    }
}
