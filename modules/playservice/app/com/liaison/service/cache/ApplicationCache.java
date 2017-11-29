package com.liaison.service.cache;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.liaison.service.models.Message;

import play.cache.AsyncCacheApi;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Class uses the caches
 *
 */
public class ApplicationCache extends Controller {

    private AsyncCacheApi cache;

    private static final String KEY_CACHE = "Cache_Key";

    private static final String VALUE_CACHE = "Default_Cache_Value";

    private static final int TIME_TO_LIVE = 60 * 5; // 5 minutes

    @Inject
    public ApplicationCache(AsyncCacheApi cache) {
        this.cache = cache;
    }

    /**
     * Method to add the cache
     * 
     * @return result
     */
    public Result addToCache() {

        JsonNode jsonMessage = request().body().asJson();
        Message ValueTocache = Json.fromJson(jsonMessage, Message.class);
        cache.set(KEY_CACHE, ValueTocache.getMsgValue(), TIME_TO_LIVE);
        return ok("Cache " + ValueTocache.getMsgValue() + " added successfully");
    }

    /**
     * Method to retrive the cache
     * 
     * @return result
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Result getCache() throws InterruptedException, ExecutionException {

        CompletionStage<String> cachedValue = cache.get(KEY_CACHE);
        String value = cachedValue.toCompletableFuture().get();
        return ok("Cache value: " + value);
    }

    /**
     * Method to get or update the cache if not available.
     * 
     * @return result
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Result getOrUpdateCache() throws InterruptedException, ExecutionException {

        CompletionStage<String> cachedValue = cache.getOrElseUpdate(KEY_CACHE, this::lookUpAndUpdate, TIME_TO_LIVE);
        String value = cachedValue.toCompletableFuture().get();
        return ok("Cache value: " + value);
    }

    /**
     * Helper method to update the new cache value.
     * 
     * @return cacheValue
     */
    private CompletionStage<String> lookUpAndUpdate() {
        return CompletableFuture.completedFuture(VALUE_CACHE);
    }
}
