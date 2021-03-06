package org.kyantra.resources;

import com.amazonaws.services.iot.model.CreateKeysAndCertificateRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateResult;
import org.kyantra.beans.ThingBean;
import org.kyantra.dao.ThingDAO;
import org.kyantra.utils.AwsIotHelper;
import org.kyantra.utils.StringConstants;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.Paths;

public class CertificateResource {

    @GET
    @Path("get/{name}/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response get(@PathParam("id") Integer id,
                        @PathParam("name") String name) {

        ThingBean bean = ThingDAO.getInstance().get(id);
        String certificateDirectory = Paths.get(StringConstants.CERT_ROOT,bean.getCertificateDir(),name+".pem").toString();
        File certificateFile = new File(certificateDirectory);
        System.out.println(certificateFile.getAbsolutePath()+"\nExists: " + certificateFile.exists());

        if(certificateFile.exists()) {
            return Response.ok(certificateFile, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + certificateFile.getName() + "\"" ) //optional
                    .build();
        }
        return Response.status(404).build();
    }

}
