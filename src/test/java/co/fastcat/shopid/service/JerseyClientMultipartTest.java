package co.fastcat.shopid.service;

import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

/**
 * Created by swsong on 2015. 8. 18..
 */
public class JerseyClientMultipartTest {

    @Test
    public void testFileUpload(){
        String clusterId = "dev";
        String appId = "cedi";

        String uri = String.format("/v1/clusters/%s/apps/%s/file", clusterId, appId);
        Client client = ClientBuilder.newClient().register(MultiPartFeature.class);
        WebTarget webTarget = client.target("http://localhost:9000").path(uri);

        MultiPart multiPart = new MultiPart();
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",
                new File("/private/tmp/garuda/upload/fastcat/cEDI_150518.war"),
                MediaType.APPLICATION_OCTET_STREAM_TYPE);
        multiPart.bodyPart(fileDataBodyPart);

        Response response = webTarget.request()
                .post(Entity.entity(multiPart, multiPart.getMediaType()));

        System.out.println(response.getStatus() + " "
                + response.getStatusInfo() + " " + response);

    }
}
