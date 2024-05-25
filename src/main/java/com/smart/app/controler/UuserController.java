package com.smart.app.controler;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.smart.app.enties.Contact;
import com.smart.app.enties.User;
import com.smart.app.helper.Message;
import com.smart.app.repository.ContactRepo;
import com.smart.app.repository.UserRepository;


@Controller
@RequestMapping("/user")
public class UuserController {
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ContactRepo contactRepo;
	private String username;
	//  method for get user and common data
	@ModelAttribute
	public void addCommonData(Model model ,Principal principal) {
		//  it is using to get username from session      
		this.username = principal.getName();

		System.out.println("username "+ username);
		// finding user deatil by ussrname
		User user =  this.userRepo.getUserByUsername(username);
		//     System.out.println("user "+ user.toString());
		model.addAttribute(user);

	}

	// user dashboard home
	@RequestMapping("/index")
	public String dashboard( Model model,Principal principal) {
		return "normal/user_dashboard";
	}

	//  open add form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title","Add contact");
		model.addAttribute("contact", new Contact() );
		return "normal/add_contact_form";
	}


	// submit add contact details 
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,@RequestParam("images") MultipartFile file, Principal principle,HttpSession session) {
		// Object name =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			String name =  principle.getName();
			System.out.println(name); 

			User user = this.userRepo.getUserByUsername(name);

			//  processing and uploading file

			if(file.isEmpty()) {
				System.out.println("file is empty");
				contact.setImage("default.png");
			}

			else {
				contact.setImage(file.getOriginalFilename());
				File saveFiles=  new ClassPathResource("static/img").getFile();
				Path path=   Paths.get(saveFiles.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("image uploaded");
			}

			contact.setUser(user); 
			user.getContacts().add(contact);
			user.getContacts().add(contact);
			this.userRepo.save(user);

			//success message
			session.setAttribute("message", new Message("your contact is add successfuly !","success"));
			System.out.println("Contact = "+contact );
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			e.printStackTrace();

			//error message
			session.setAttribute("message", new Message("somthing went to wrong !","danger"));

		}


		return "normal/add_contact_form";
	}

	//  view all contact list
	// per page =5[n]
	//current page =[page]
	@GetMapping("/view-contact/{page}")
	public String viewContact(@PathVariable("page") Integer page, Model m,Principal principal) {
		m.addAttribute("title","List of all contact");
		String username =	principal.getName();
		User user=      this.userRepo.getUserByUsername(username);
		Pageable pageable = PageRequest.of(page, 5);
		Page<Contact> contact =	this.contactRepo.findAllContactByUserId(user.getId(),pageable);
		m.addAttribute("contacts",contact);
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages",contact.getTotalPages());
		return "normal/view-contact";
	}

	//  showing perticular contact
	@GetMapping("/show-contact/{cId}/details")
	public String showContactDetails(@PathVariable("cId") Integer cId,Model m,Principal principal){
		System.out.println("cId "+cId);
		Optional<Contact> optionalContact  =  this.contactRepo.findById(cId);
		Contact contact =  optionalContact.get();
		User user =  this.userRepo.getUserByUsername(principal.getName());
		if(user.getId()==contact.getUser().getId()) 
			
		
		
		
		m.addAttribute("contact", contact);
		return "normal/show-conatct-details";
	}
   // delete contact	
	@GetMapping("/delete/contact/{cid}")
	public String userDeleteContact(@PathVariable("cid") Integer cid,Principal principal,HttpSession httpSession) throws IOException {
		User user =  this.userRepo.getUserByUsername(principal.getName());
		Contact contct = this.contactRepo.findById(cid).get();
		//if(user.getId()==contct.getUser().getId()) 
			
		//contct.setUser(null);
		//this.contactRepo.delete(contct);
		
		// bug fixing
		
		User user1 =  this.userRepo.getUserByUsername(principal.getName());
		user.getContacts().remove(contct);
		this.userRepo.save(user1);
		File file =  new ClassPathResource("static/img").getFile();
		File file1 =  new File(file, contct.getImage());
		 file1.delete();
		System.out.println("delete");
		
		httpSession.setAttribute("message",new Message("contact deleted Successfuly ", "success"));
	      System.out.println("see");
		return "redirect:/user/view-contact/0";
	}
	
	// open update form
	@PostMapping("/open-updte-form/{cid}")
	public String openUpdateForm(@PathVariable("cid") Integer cid,Model m) {
 
		m.addAttribute("title", "update form");
		     Contact contact = this.contactRepo.findById(cid).get();
		m.addAttribute("contact", contact);
		return "normal/open-update";
	}
	
	
	// update contact
	@RequestMapping(value =  "/process-update" ,method = RequestMethod.POST)
	public String updateContact(@ModelAttribute Contact contact,@RequestParam("images") MultipartFile file, Model model,HttpSession httpSession,Principal principal) {
		Contact oldContactDetails =  this.contactRepo.findById(contact.getcId()).get();
		try {
			if (!file.isEmpty()) {
				// file work and rewrite
				
				//old photo delete
				File deleteFile =  new ClassPathResource("static/img").getFile();
				File  file1 =  new File(deleteFile, oldContactDetails.getImage());
				file1.delete();
				
				// update new photo
				
				File saveFile =  new ClassPathResource("static/img").getFile();
				Path path =  Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
			}
			else {
				contact.setImage(oldContactDetails.getImage());
			}
			User user = this.userRepo.getUserByUsername(principal.getName());
			contact.setUser(user);
			this.contactRepo.save(contact);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "redirect:/user/show-contact/"+contact.getcId()+"/details";
	}
	
	// your profile
	@GetMapping("/profile")
	public String profile(Model model) {
		model.addAttribute("title","profilePage");
		return "normal/profile";
	}
	
	// open settng handler
	@GetMapping("/settings")
	public String settingsOpen() {
		return "normal/settings";
	}
	
	//  change password
	@RequestMapping(value = "/change-password",method = RequestMethod.POST)
	public String changePassword(Principal principal,@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,HttpSession httpSession) {
		System.out.println("old "+oldPassword );
		System.out.println("new "+newPassword);
		User user =  this.userRepo.getUserByUsername(principal.getName());
		System.out.println("pw "+user.getPassword());
		if(this.bcryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(bcryptPasswordEncoder.encode(newPassword));
			this.userRepo.save(user);
			httpSession.setAttribute("message", new Message("your password change successfuly !", "alert-success"));
		}
		else {
			httpSession.setAttribute("message", new Message("your old password is wrong !", "alert-danger"));
			return "redirect:/user/settings";
		}
		return "redirect:/user/index";
	}
}
