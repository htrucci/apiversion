package com.htrucci.apiversion.controller;

import com.htrucci.apiversion.config.ApiVersion;
import com.htrucci.apiversion.vo.Version;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/service/api/{version}/")
@ApiVersion(version = Version.V3)
public class ApiTobeController {

	@RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
	public String testv3(HttpServletRequest httpReq, Model model) throws Exception{
		return "test";
	}

	@ApiVersion(version = {Version.V4, Version.V5})
	@RequestMapping(value = "/{version}/test", method = {RequestMethod.GET, RequestMethod.POST})
	public String testv45(HttpServletRequest httpReq, Model model) throws Exception{
		return "test";
	}
}
