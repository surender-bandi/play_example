package com.liaison.service.play.nucleus;

import com.fasterxml.jackson.databind.JsonNode;

import play.Logger.ALogger;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;

import javax.inject.Inject;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import com.liaison.service.controllers.BasicAuthAction;

@With(PostAction.class)
public class PostController extends Controller {

    private static final ALogger logger = play.Logger.of("application");
    private HttpExecutionContext ec;
    private PostResourceHandler handler;

    @Inject
    public PostController(HttpExecutionContext ec, PostResourceHandler handler) {
        this.ec = ec;
        this.handler = handler;
    }

    @With(BasicAuthAction.class)
    public CompletionStage<Result> list() {
        logger.info("Show the list of result");
        return handler.find().thenApplyAsync(posts -> {
            final List<PostResource> postList = posts.collect(Collectors.toList());
            return ok(Json.toJson(postList));
        }, ec.current());
    }

    @With(BasicAuthAction.class)
    public CompletionStage<Result> show(String id) {
        logger.info("Show the specific id result", id);
        return handler.lookup(id).thenApplyAsync(optionalResource -> {
            return optionalResource.map(resource ->
                ok(Json.toJson(resource))
            ).orElseGet(() ->
                notFound()
            );
        }, ec.current());
    }

    @With(BasicAuthAction.class)
    public CompletionStage<Result> update(String id) {
        logger.info("Update information for id", id);
        JsonNode json = request().body().asJson();
        PostResource resource = Json.fromJson(json, PostResource.class);
        return handler.update(id, resource).thenApplyAsync(optionalResource -> {
            return optionalResource.map(r ->
                    ok(Json.toJson(r))
            ).orElseGet(() ->
                    notFound()
            );
        }, ec.current());
    }

    @With(BasicAuthAction.class)
    public CompletionStage<Result> create() {
        logger.info("Create information");
        JsonNode json = request().body().asJson();
        final PostResource resource = Json.fromJson(json, PostResource.class);
        return handler.create(resource).thenApplyAsync(savedResource -> {
            return created(Json.toJson(savedResource));
        }, ec.current());
    }
}
