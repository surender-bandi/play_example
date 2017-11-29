package com.liaison.service.error;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.api.client.http.HttpStatusCodes;

import play.Logger.ALogger;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.http.HttpErrorHandlerExceptions;
import play.http.HttpErrorHandler;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

/**
 * Application level error handling class.
 *
 */
@Singleton
public class ErrorHandler implements  HttpErrorHandler{

    private static final ALogger logger = play.Logger.of("application");

    private final OptionalSourceMapper sourceMapper;

    @Inject
    public ErrorHandler(OptionalSourceMapper sourceMapper) {
        this.sourceMapper = sourceMapper;
    }

    @Override
    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode,
            String message) {

        logger.error("Client error occured: " + message);

        if (HttpStatusCodes.STATUS_CODE_NOT_FOUND == statusCode) {
            return onNotFound(request);
        } else {
            return CompletableFuture.completedFuture(Results.status(statusCode, views.html.errors.badRequest.render(
                    request.method(), request.uri(), message)));
        }

    }

    @Override
    public CompletionStage<Result> onServerError(RequestHeader request,
            Throwable exception) {

        logger.error("Server error occured: " + exception.getMessage(), exception);
        
        try {

        	UsefulException usefulException = throwableToUsefulException(exception);
        	return CompletableFuture.completedFuture(Results.internalServerError(views.html.errors.error.render(usefulException)));
        } catch (Exception e) {
        	logger.error("Error while handling error", e);
            return CompletableFuture.completedFuture(Results.internalServerError());
        }
    }

    /*
     *  Helper method invokes when requested resource is not found
     *  
     *  @param request The user request.
     *  @return a CompletionStage containing the Result.
     */
    protected CompletionStage<Result> onNotFound(RequestHeader request) {
    	logger.error("In not found");
        return CompletableFuture.completedFuture(Results.notFound(views.html.errors.notFound.render(
                request.method(), request.uri())));
    }

    /*
     * Convert the given exception to an exception that Play can report more information about.
     * 
     * @param throwable the throwable exception
     * @return UsefulException thatcontains generic details.
     */
    protected final UsefulException throwableToUsefulException(final Throwable throwable) {
    	return HttpErrorHandlerExceptions.throwableToUsefulException(sourceMapper.sourceMapper(), true, throwable);
    }
}
