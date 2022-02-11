package com.xlibrpkg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import jakarta.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

import static com.xlibrpkg.ClientRequest.RequestType.*;
import static com.xlibrpkg.XLibrGlobals.*;

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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
		Parent root = loader.load();
		mainStage = primaryStage;

		mainStage.setTitle("XLibr Login");
		mainStage.setScene(new Scene(root));
		mainStage.setResizable(false);

		mainStage.show();

	}

	@Override
	public void stop() {
		// When client closes correctly the message is sent to the server to terminate the connection on both ends
		try {
			Log.INFO("Closing connection!");
			s_Request.value = CLOSECONNECTION;
			s_Xlibrconnect.SendObject(s_Request);
		} catch (Exception e) {
			Log.CRITICAL("Connection severed");
		}
	}
}
