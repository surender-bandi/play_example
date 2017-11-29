package com.liaison.service.filters;

import javax.inject.Inject;

import play.filters.gzip.GzipFilter;
import play.http.DefaultHttpFilters;

/**
 * Application level filter class
 *
 */
public class ApplicationFilters extends DefaultHttpFilters {

    @Inject
    public ApplicationFilters(GzipFilter gzip, LoggingFilter logging) {
        super(gzip, logging);
    }
}
