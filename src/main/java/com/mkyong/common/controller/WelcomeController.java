package com.mkyong.common.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mkyong.common.dao.AccountStatement;
import com.mkyong.common.dao.StatementDaoImpl;
import com.mkyong.common.service.Data;
import com.mkyong.common.service.DataValidator;

@Controller

public class WelcomeController {
	

    
    DataValidator dataValidator = new DataValidator();   
    private static final String ADMIN = "admin";
    private static final String MESSAGE = "message";

    @RequestMapping(value = "/admin",method = RequestMethod.GET)
	public String welcomeAdmin(ModelMap model) {

		model.addAttribute(MESSAGE, "Spring Security - ROLE_ADMIN");
		return ADMIN;

	}

    @RequestMapping(value = "/user",method = RequestMethod.GET)
	public String welcomeUser(ModelMap model) {

		model.addAttribute(MESSAGE, "Spring Security - ROLE_USER");
		return "user";

	}
    @RequestMapping(value = "fetch",method = RequestMethod.POST)
    public ModelAndView submit(Data data,ModelAndView model) throws ParseException {
		model.addObject(MESSAGE, "Spring Security - Data");
		String validationMsgs = dataValidator.validateData(data);
		if(validationMsgs != null) {
			if(validationMsgs.equals("403")) {
				model.setViewName("/403");
			}
			else {
			model.addObject("validations", validationMsgs);
			model.setViewName(ADMIN);
			}
			
		}
		else
		{
			StatementDaoImpl impl = new StatementDaoImpl();
			List<AccountStatement> list = impl.getStatementList(data);
			model.addObject("listEmp", list);
			model.setViewName("data");
		}
		return model;
    }
	
}