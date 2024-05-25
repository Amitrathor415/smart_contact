package com.smart.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.app.enties.Contact;
import com.smart.app.enties.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Integer> {

	@Query("select c from contact c where c.user.id=:userId")
	public Page<Contact> findAllContactByUserId(@Param("userId") int userId,Pageable pageable);

public List<Contact> findByfNameContainingAndUser(String fName,User user);

}
