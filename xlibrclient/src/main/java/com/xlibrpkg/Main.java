package com.xlibrpkg;

import jakarta.xml.bind.DatatypeConverter;

import java.security.MessageDigest;
import com.xlibrpkg.ClientRequest;

import static com.xlibrpkg.ClientRequest.RequestType.LOGIN;
import static com.xlibrpkg.ClientRequest.RequestType.SIGNUP;
import static com.xlibrpkg.ClientRequest.RequestType.CLOSECONNECTION;

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
		Log.getInstance();

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
		request.value = LOGIN;

		try {
			XLibrconnect xlibrconnect = new XLibrconnect("localhost", 23313);
			xlibrconnect.SendObject(request);
			xlibrconnect.SendObject(userData);
			request.value = SIGNUP;
			xlibrconnect.SendObject(request);
			xlibrconnect.SendObject(userData);
			request.value = CLOSECONNECTION;
			xlibrconnect.SendObject(request);

		} catch (Exception exp) {
			Log.CRITICAL("Unable to connect to server!");
		}
	}



}
