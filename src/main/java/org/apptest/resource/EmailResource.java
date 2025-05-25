package org.apptest.resource;
import org.apptest.service.EmailService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/send")
public class EmailResource {
    @Inject
    EmailService emailService;
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    @Path("/aprove")
    public Response approve(){
        String sendTo ="sufardicoc@gmail.com";
        emailService.sendEmail(
            sendTo,
            "kita coba dulu",
            "Selamat malam, mas Gun selamat anda diterima Di kost Biru"
        );
        return Response.ok("Berhsil Mengirimkan Email :" + sendTo).build();
    }
    
}
