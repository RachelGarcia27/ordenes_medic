package com.racheldev.orders.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login(@RequestParam(value="error", required=false) String error, Model model, Principal principal, RedirectAttributes falsh) {
		
		if(error != null) {
			
		}
		
		if(principal != null) {
			falsh.addFlashAttribute("Info", "Ya estabas logeado");
			return "redirect:/";
		}
		
		return "login";
	}
	
}
