package ru.perm.v.hr_admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	@RequestMapping("/")
	public String welcome() {
		return "/employees/"; // employees — имя представления, которое будет отображаться по умолчанию
	}
}
