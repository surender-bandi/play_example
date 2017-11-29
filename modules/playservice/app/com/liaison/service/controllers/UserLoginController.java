package com.liaison.service.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.With;

/**
 * Controller class to validate user
 *
 */
public class UserLoginController extends Controller {

	@With(BasicAuthAction.class)
	public Result authAndProceed() {
		String userName = ctx().args.get("USER").toString();
		return ok("Successful login with user " + userName);
	}
}
