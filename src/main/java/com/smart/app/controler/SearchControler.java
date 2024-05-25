package com.smart.app.controler;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.app.enties.Contact;
import com.smart.app.enties.User;
import com.smart.app.repository.ContactRepo;
import com.smart.app.repository.UserRepository;

@RestController
@CrossOrigin
public class SearchControler {
	@Autowired
private UserRepository userRepository;
	@Autowired
	private ContactRepo contactRepo;
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal){
	User user =  this.userRepository.getUserByUsername(principal.getName());
	List<Contact> contacts= this.contactRepo.findByfNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}
}
