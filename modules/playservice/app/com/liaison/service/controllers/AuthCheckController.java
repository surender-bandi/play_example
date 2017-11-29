package com.liaison.service.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

public class AuthCheckController extends Controller {

	@With(OAuthRedirect.class)
	public Result authCheck() {
		return ok("Login success");
	}
}
