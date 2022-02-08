package com.xlibrpkg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import jakarta.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.Objects;

import static com.xlibrpkg.ClientRequest.RequestType.*;


public class XLibrApplication extends Application {

	static public Stage mainStage;

	public static String GetHash(byte[] _input, String _algorithm) {
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

	public static void main(String[] args) {launch(args);}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Log.getInstance();
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-view.fxml")));
		mainStage = primaryStage;
		mainStage.setTitle("XLibr Login");
		mainStage.setScene(new Scene(root));
		mainStage.setResizable(false);
		mainStage.show();
	}

	private void xStart() {
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

		XLibrApplication app = new XLibrApplication();

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
