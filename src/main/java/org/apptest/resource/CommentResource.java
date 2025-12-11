package org.apptest.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apptest.entity.Comment;
import org.apptest.param.CommentRequest;
import org.apptest.response.ApiResponse;
import org.apptest.service.CommentService;

import java.util.List;

@Path("/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentResource {

    @Inject
    CommentService commentService;

    @GET
    public Response list(@QueryParam("limit") @DefaultValue("50") int limit) {
        List<Comment> comments = commentService.listComments(limit);
        return Response.ok(ApiResponse.ok(comments, "Daftar komentar")).build();
    }

    @POST
    public Response create(CommentRequest request) {
        if (request.name == null || request.name.trim().isEmpty()
                || request.message == null || request.message.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.fail("Nama dan pesan wajib diisi"))
                    .build();
        }

        Comment c = commentService.addComment(request);
        return Response.ok(ApiResponse.ok(c, "Komentar tersimpan")).build();
    }
}
