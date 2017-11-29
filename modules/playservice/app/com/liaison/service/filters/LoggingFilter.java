package com.liaison.service.filters;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import javax.inject.Inject;

import akka.stream.Materializer;
import play.Logger.ALogger;
import play.mvc.Filter;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

/**
 * Simple filter class used to check and log the time taken for the particular request.
 *
 */
public class LoggingFilter extends Filter {

    private static final ALogger logger = play.Logger.of("application");

    @Inject
    public LoggingFilter(Materializer materializer) {
        super(materializer);
    }

    @Override
    public CompletionStage<Result> apply(
            Function<RequestHeader, CompletionStage<Result>> nextFilterCtrlr,
            RequestHeader requestHeader) {
        logger.info("LoggingFilter invoked");
        long initTime = System.currentTimeMillis();
        return nextFilterCtrlr.apply(requestHeader).thenApply(result -> {

            long endTime = System.currentTimeMillis();
            long totalTimeTaken = endTime - initTime;
            logger.info("{} {} took {} ms and returned status is {}",
                    requestHeader.method(), requestHeader.uri(), totalTimeTaken, result.status());
            return result.withHeader("Reqest-total-time", "" + totalTimeTaken);
        });
    }
}
