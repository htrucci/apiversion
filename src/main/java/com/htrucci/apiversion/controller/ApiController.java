package com.htrucci.apiversion.controller;

import com.htrucci.apiversion.config.ApiVersion;
import com.htrucci.apiversion.vo.Version;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/service/api/")
public class ApiController {

	@RequestMapping(value = "test", method = {RequestMethod.GET, RequestMethod.POST})
	public void testv1(HttpServletRequest httpReq, Model model) throws Exception{
		System.out.println("test1");
	}

	@ApiVersion(version = Version.V2)
	@RequestMapping(value = "/{version}/test", method = {RequestMethod.GET, RequestMethod.POST})
	public void testv2(HttpServletRequest httpReq, Model model) throws Exception{
		System.out.println("testv2");
	}
}
