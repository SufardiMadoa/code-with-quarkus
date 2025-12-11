package org.apptest.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apptest.entity.Rsvp;
import org.apptest.param.RsvpRequest;
import org.apptest.response.ApiResponse;
import org.apptest.response.RsvpStatusResponse;
import org.apptest.service.RsvpService;

@Path("/rsvp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RsvpResource {

    @Inject
    RsvpService rsvpService;

    @GET
    @Path("/{guestCode}")
    public Response getRsvp(@PathParam("guestCode") String guestCode) {
        Rsvp rsvp = rsvpService.findByGuestCode(guestCode);
        if (rsvp == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.fail("RSVP tidak ditemukan"))
                    .build();
        }
        return Response.ok(ApiResponse.ok(rsvp, "RSVP ditemukan")).build();
    }

    @POST
    public Response submitRsvp(RsvpRequest request) {
        if (request.guestCode == null || request.guestName == null || request.attendance == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.fail("guestCode, guestName, dan attendance wajib diisi"))
                    .build();
        }

        Rsvp saved = rsvpService.saveOrUpdate(request);
        return Response.ok(ApiResponse.ok(saved, "RSVP tersimpan")).build();
    }

    @GET
    @Path("/stats")
    public Response stats() {
        RsvpStatusResponse stats = rsvpService.getStats();
        return Response.ok(ApiResponse.ok(stats, "Statistik RSVP")).build();
    }
}
