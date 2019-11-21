package de.aqua.web.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Created by bdumitru on 7/12/2017.
 */
public class SSLUtil {

    /**
     * Returns SSL connection client considering if there are or not proxy settings
     *
     * @param proxy list containing the proxy host and port (when is required)
     * @return new Client instance
     */
    public static Client getSSLClient(final List<String> proxy) throws KeyManagementException,
            NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        HostnameVerifier hv = getHostnameVerifier();
        ClientConfig config = new DefaultClientConfig();
        SSLContext ctx = getSSLContext();
        config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                new HTTPSProperties(hv, ctx));

        Client client;
        if (proxy.size() > 1) {
            client = new Client(new URLConnectionClientHandler(new HttpURLConnectionFactory() {
                Proxy p = null;

                public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
                    if (p == null) {
                        p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy.get(0),
                                Integer.parseInt(proxy.get(1))));
                    }
                    return (HttpURLConnection) url.openConnection(p);
                }
            }), config);
        } else
            client = Client.create(config);

        return client;
    }

    /**
     * Returns boolean value regarding the host validation
     *
     * @return true or false
     */
    private static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hv = (hostname, session) -> true;

        return hv;
    }

    /**
     * Returns the SSL context and sets as trusted all the certificates, without being necessary to
     * import them
     */
    private static SSLContext getSSLContext() throws NoSuchAlgorithmException,
            KeyManagementException, KeyStoreException, CertificateException, IOException {

//        InputStream is = APICallUtil.class.getResourceAsStream("src/test/resources/tomcat.crt");
//        CertificateFactory cf = CertificateFactory.getInstance("X.509");
//        X509Certificate caCert = (X509Certificate) cf.generateCertificate(is);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory
                .getDefaultAlgorithm());
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null);
//        ks.setCertificateEntry("caCert", caCert);

        tmf.init(ks);

        SSLContext sslContext = SSLContext.getInstance("TLSv1");
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

        }}, new SecureRandom());

        return sslContext;
    }
}


