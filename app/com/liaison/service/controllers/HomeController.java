package com.liaison.service.controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class HomeController extends Controller {

	public Result indexHome() {
		return ok(views.html.index.render());
	}
}
