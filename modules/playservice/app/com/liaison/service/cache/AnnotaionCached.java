package com.liaison.service.cache;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.liaison.service.models.Message;

import play.Logger.ALogger;
import play.cache.AsyncCacheApi;
import play.cache.Cached;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Class that uses the cache annotation @cached for the http responses
 *
 */
public class AnnotaionCached extends Controller {

    private static final ALogger logger = play.Logger.of("application");

    private static final String RESULT = "result";

    private AsyncCacheApi cache;

    @Inject
    public AnnotaionCached(AsyncCacheApi cache) {
        logger.info("Instantiating the async cache");
        this.cache = cache;
    }

    @Cached(key = RESULT, duration = 30)
    public Result cacheRequest() {

        JsonNode jsonMessage = request().body().asJson();
        Message ValueTocache = Json.fromJson(jsonMessage, Message.class);
        logger.info("value to cache is: " + ValueTocache);
        return ok("Cached value: " + ValueTocache.getMsgValue());
    }

    /**
     *  Class that returns the cached result of the previous method
     *  
     * @return result
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Result getResultCache() throws InterruptedException, ExecutionException {

        CompletionStage<Result> cacheResult = cache.get(RESULT);
        Result value = cacheResult.toCompletableFuture().get();
        int statusOfResult = value.status();
        return ok(statusOfResult + " is cached result");
	}
}
