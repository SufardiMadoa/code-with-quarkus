package org.apptest.resource;
import org.apptest.param.LoginParam;
import org.apptest.param.RegisterParam;
import org.apptest.response.LoginResponse;
import org.apptest.service.UserService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UserService userService;

     @POST
    @Path("/register")
    public Response register(RegisterParam param) {
        String result = userService.register(param);
        
        if (result.equals("Registrasi berhasil")) {
            return Response.ok()
                    .entity("{\"message\": \"" + result + "\"}")
                    .build();
        } else {
            return Response.status(400)
                    .entity("{\"message\": \"" + result + "\"}")
                    .build();
        }
    }
     @GET
    @Path("/verify")
    public Response verifyEmail(@QueryParam("token") String token) {
        userService.verifyEmail(token);
        return Response.ok("Email berhasil diverifikasi").build();
    }
    
@POST
@Path("/login")
public Response login(LoginParam param) {
    try {
        LoginResponse response = userService.login(param);
        return Response.ok(response).build();
    } catch (Exception e) {
        return Response.status(500)
                .entity("Database Error: " + e.getMessage())
                .build();
    }
}
    //  @POST
    // @Path("/login")
    // public Response login(LoginParam loginParam) {
    //     try {
    //         LoginResponse response = userService.login(loginParam);
    //         return Response.ok(response).build();
    //     } catch (Exception e) {
    //         return Response.status(Response.Status.fromStatusCode(
    //             e instanceof jakarta.ws.rs.WebApplicationException 
    //                 ? ((jakarta.ws.rs.WebApplicationException) e).getResponse().getStatus() 
    //                 : 500))
    //             .entity(new ErrorResponse(e.getMessage()))
    //             .build();
    //     }
    // }
    //    public static class ErrorResponse {
    //     public String error;
        
    //     public ErrorResponse(String error) {
    //         this.error = error;
    //     }
    // }

}