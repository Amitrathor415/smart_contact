package com.smart.app.config;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.app.enties.User;
import com.smart.app.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {
 @Autowired
	private UserRepository userRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		User user = userRepo.getUserByUsername(username);
		
		if(user==null) {
			throw new UsernameNotFoundException("user not found !");
		}
		
		CusomUserDetails cusomUserDetails =  new CusomUserDetails(user);
		return cusomUserDetails;
	}

}
