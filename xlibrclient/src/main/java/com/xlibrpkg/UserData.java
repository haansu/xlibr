package com.xlibrpkg;

import java.io.Serializable;

public class UserData implements Serializable {

	String username;
	String firstname;
	String lastname;
	String email;
	String address;
	String password;

	UserData() {
		username = null;
		firstname = null;
		lastname = null;
		email = null;
		address = null;
		password = null;
	}

	@Override
	public String toString() {
		return "User{" +
				"username='" + username + '\'' +
				", firstname='" + firstname + '\'' +
				", lastname='" + lastname + '\'' +
				", email='" + email + '\'' +
				", address='" + address + '\'' +
				", password='" + password + '\'' +
				'}';
	}

	public String getUsername() {
		return username;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}