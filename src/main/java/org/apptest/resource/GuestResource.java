package org.apptest.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apptest.entity.Guest;
import org.apptest.response.ApiResponse;
import org.apptest.service.GuestService;

@Path("/guests")
@Produces(MediaType.APPLICATION_JSON)
public class GuestResource {

    @Inject
    GuestService guestService;

    @GET
    @Path("/{code}")
    public Response getByCode(@PathParam("code") String code) {
        Guest guest = guestService.findByCode(code);
        if (guest == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.fail("Guest tidak ditemukan"))
                    .build();
        }
        return Response.ok(ApiResponse.ok(guest, "Guest ditemukan")).build();
    }
}
