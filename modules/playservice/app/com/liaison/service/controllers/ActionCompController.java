package com.liaison.service.controllers;

import java.util.concurrent.CompletionStage;

import play.Logger.ALogger;
import play.mvc.Action.Simple;
import play.mvc.Controller;
import play.mvc.With;
import play.mvc.Http.Context;
import play.mvc.Result;

public class ActionCompController extends Controller {

	private static final ALogger logger = play.Logger.of("application");
	
	private static final String DATA = "Data";

	public static class DataAction extends Simple {
		@Override
		public CompletionStage<Result> call(Context ctx) {
			logger.info("Calling action for {}", ctx);
			ctx().args.put(DATA, "Default Data");
			return delegate.call(ctx);
		}
	}

	@With(DataAction.class)
	public Result getActionData() {
		return ok("data action works, data is: " + ctx().args.get(DATA));
	}
}
