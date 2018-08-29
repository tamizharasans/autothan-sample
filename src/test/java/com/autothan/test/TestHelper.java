package com.autothan.test;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import java.util.UUID;

/**
 * Created by dsastry on 29/08/18.
 */
public class TestHelper {

    public static Client client = null;
    public static String BASE_URI = "https://en.wikipedia.org/wiki/The_Shawshank_Redemption";


    public Invocation.Builder createBuilder(String path) {
        WebTarget webTarget = getClient().target(path);
        Invocation.Builder builder = webTarget.request();


        String prefix = "TEST-";
        String uuid = UUID.randomUUID().toString();
        uuid = prefix.concat(uuid.substring(14));

        return builder;
    }

    // Gets a jersey client Object
    public static Client getClient() {
        if (client == null) {
            client = ClientBuilder.newClient();
            client.register(new LoggingFilter());
            client.register(MultiPartFeature.class);
        }
        return client;
    }

}
