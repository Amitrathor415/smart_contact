package com.smart.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.app.enties.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from user u where u.email= :email")
	public User getUserByUsername(@Param("email") String email );
    
  //  public boolean emailIsExits(String email);
}
