package com.smart.app.enties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "contact")
@Table(name = "CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
private int cId;
private String fName;
private String lName;
private String  work;
@Column(unique = true)
private String email;

private String phone;
private String image;
@Column(length = 5000)
private String description;

@ManyToOne
@JsonIgnore
private User user;


public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
public int getcId() {
	return cId;
}
public void setcId(int cId) {
	this.cId = cId;
}
public String getfName() {
	return fName;
}
public void setfName(String fName) {
	this.fName = fName;
}
public String getlName() {
	return lName;
}
public void setlName(String lName) {
	this.lName = lName;
}
public String getWork() {
	return work;
}
public void setWork(String work) {
	this.work = work;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getImage() {
	return image;
}
public void setImage(String image) {
	this.image = image;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}


@Override
public boolean equals(Object obj) {
	// TODO Auto-generated method stub
	return this.cId== ((Contact)obj).getcId();
}
@Override
public String toString() {
	return "Contact [cId=" + cId + ", fName=" + fName + ", lName=" + lName + ", work=" + work + ", email=" + email
			+ ", phone=" + phone + ", image=" + image + ", description=" + description + ", user=" + user + "]";
}

}
