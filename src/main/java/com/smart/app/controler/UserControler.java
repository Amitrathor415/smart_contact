package com.smart.app.controler;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.smart.app.enties.User;
import com.smart.app.helper.Message;
import com.smart.app.repository.UserRepository;


@Controller
public class UserControler {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title","Home -  smart contact manager");
		//User u = this.userRepository.getUserByUsername("jhon@gmail.com");
		//System.out.println(" u = "+u.toString());
		return "home";
	}
	@RequestMapping("/signup")
	public String signup(Model model){
		model.addAttribute("title","Signup -  smart contact manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	@RequestMapping(value = "/do_register" , method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user")User user,final BindingResult result1 ,@RequestParam(value = "agreement", defaultValue = "false" ) boolean agreement,
		 Model model,HttpSession session) {
		try {
			if (!agreement) {
				System.out.println("you have not agrred terms and condition");
			   throw new Exception("you have not agrred terms and condition");
			}
			 if(result1.hasErrors()) {
				System.out.println("Error "+result1.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			user.setRole("ROLR_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agrrement "+ agreement);
			System.out.println("User "+user);
			User result =this.userRepository.save(user);
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("successfully registerd !!", "alert-success"));
			return "/signup";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("somthimg went to wrong !!"+e.getMessage(), "alert-danger"));
			return "/signup";
		}
		
	}
// custo login
	@GetMapping("/login2")
	public String customLogin(Model model) {
		return null;
	}
}
