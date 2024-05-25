package com.smart.app.controler;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.smart.app.enties.User;
import com.smart.app.repository.UserRepository;
import com.smart.app.service.EmailService;



@Controller
public class ForgetPasswordControler {
	@Autowired
	private EmailService emailSrvice;
	@Autowired
	private UserRepository userRepo;
	@RequestMapping("/forgetPassword")
	public String openForgetpassword() {
		return "/forgetPassword";
	}
	@PostMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email,Model model,HttpSession httpSession) {

		model.addAttribute("email", email);

		User user =	this.userRepo.getUserByUsername(email);
		//		if(user.) {
		//			throw new UserPrincipalNotFoundException("user not found");
		//		}
		Random  random = new Random(1000);
		int otp=	random.nextInt(9999999);

		System.out.println("ran "+otp);

		// write code for email otp

		String subject = "OTP FROM SCM";
		String message ="<div style='border:1px solid #e2e2e2; padding:20px'>"
				+"<h1>"+"OTP is"+"<b>"+otp+"</n>"+"</h1>"+

				"</div>";
		String to  ="jhonjava122@gmail.com";
		boolean flage =	this.emailSrvice.sendEmail(subject, message, to);
		if(flage) {
			httpSession.setAttribute("email", email);
			httpSession.setAttribute("otp", otp);


			return "varify_otp";
		}	
		else {
			httpSession.setAttribute("message", "please check your email id !");
			return "forgetPassword";
		}

	}
}
