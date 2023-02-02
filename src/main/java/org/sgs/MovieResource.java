package org.sgs;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/movies")
@Tag(name = "Movie Resource", description = "Movie Rest APIs")
public class MovieResource {

    public static List<Movie> movies = new ArrayList<Movie>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getMovies",
            summary = "Get Movies",
            description = "Get all movies inside the list"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getMovies(){
        return Response.ok(movies).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    @Operation(
            operationId = "getSizeMovies",
            summary = "Get Size Movies",
            description = "Get size of the list of movies"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    public Integer getSizeMovies(){
        return movies.size();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "saveMovie",
            summary = "Register new Movie",
            description = "Create a new movie to add inside the list"
    )
    @APIResponse(
            responseCode = "201",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response saveMovie(
            @RequestBody(
                    description = "Movie to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Movie.class))
            )
            Movie addMovie){

        movies.add(addMovie);
        return Response.ok(Response.Status.CREATED).entity(movies).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/{title}")
    @Operation(
            operationId = "updateMovie",
            summary = "Update an existing Movie",
            description = "Update a movie inside the list"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response updateMovie(
            @Parameter(
                    description = "Movie Id",
                    required = true
            )
            @PathParam("id") Long id,
            @Parameter(
                    description = "Movie Title",
                    required = true
            )
            @PathParam("title") String title){

        movies.stream().map(movie -> {
            if(movie.getId().equals(id)){
                movie.setTitle(title);
            }

            return movie;
        }).collect(Collectors.toList());

        return Response.ok(movies).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Operation(
            operationId = "deleteMovie",
            summary = "Delete an existing Movie",
            description = "Delete a movie inside the list"
    )
    @APIResponse(
            responseCode = "204",
            description = "Movie deleted",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Movie not valid",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response deleteMovie(
            @Parameter(
                    description = "Movie Id",
                    required = true
            )
            @PathParam("id") Long id){

        Optional<Movie> deletedMovie = movies.stream().filter(movie -> movie.getId().equals(id)).findFirst();

        if(deletedMovie.isPresent()){
            movies.remove(deletedMovie.get());
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
