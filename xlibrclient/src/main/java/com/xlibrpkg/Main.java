package com.xlibrpkg;

import jakarta.xml.bind.DatatypeConverter;

import java.security.MessageDigest;
import com.xlibrpkg.ClientRequest;

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

	static public void main(String[] args) {

		UserData userData = new UserData();
		userData.setUsername("TheDoctor");
		userData.setFirstname("John");
		userData.setLastname("Smith");
		userData.setEmail("johnsmith@tardis.uni");
		userData.setAddress("TARDIS");
		String password = "secure";
		String hashedPass = GetHash(password.getBytes(), "SHA-256");
		userData.setPassword(hashedPass);

		System.out.println(userData);

		ClientRequest request = new ClientRequest();
		request.value = ClientRequest.RequestType.LOGIN;


		try {
			XLibrconnect xlibrconnect = new XLibrconnect("localhost", 23313);
			xlibrconnect.SendObject(request);
			xlibrconnect.SendObject(userData);
		} catch (Exception exp) {
			System.out.println("Unable to connect to the server!");
		}
	}



}
