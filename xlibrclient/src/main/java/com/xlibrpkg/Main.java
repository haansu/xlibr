package com.xlibrpkg;

import jakarta.xml.bind.DatatypeConverter;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;

public class Main {

	static public String GetHash(byte[] _input, String _algorithm) {
		String hashValue = "";
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(_algorithm);
			messageDigest.update(_input);
			byte[] digested = messageDigest.digest();
			hashValue = DatatypeConverter.printHexBinary(digested);
		} catch (Exception exp) {
			exp.printStackTrace();
		}

		return hashValue;
	}

	static public void main(String[] args) throws IOException {

		UserData userData = new UserData();
		userData.setUsername("TheDoctor");
		userData.setFirstname("John");
		userData.setLastname("Smith");
		userData.setEmail("johnsmith@tardis.uni");
		userData.setAddress("TARDIS");
		String password = "secure";
		String hashedPass = GetHash(password.getBytes(), "SHA-256");
		userData.setPassword(hashedPass);

		System.out.println(userData.toString());

		try {
			XLibrconnect xlibrconnect = new XLibrconnect("localhost", 23313);
			xlibrconnect.SendObject(userData);
		} catch (Exception exp) {
			System.out.println("Unable to connect to the server!");
		}
	}



}
