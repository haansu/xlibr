package com.xlibrpkg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

import static com.xlibrpkg.ClientRequest.RequestType.*;

public class XLibrController {

	static public XLibrConnect s_Xlibrconnect;
	static public ClientRequest s_Request;
	static public boolean allowLogin = false;

	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;

	@FXML
	public void LoginButton(ActionEvent _actionEvent) {
		String userText = usernameField.getText();
		String passText = passwordField.getText();
		Log.INFO("Username - " + userText);
		Log.INFO("Password - " + XLibrApplication.GetHash(passText.getBytes(), "Sha-256"));
		if (Login(userText, passText))
			try {
				OpenMainStage();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@FXML
	public void SignupSwitch(ActionEvent _actionEvent) throws Exception {
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signup-view.fxml")));
		XLibrApplication.mainStage.setTitle("XLibr Signup");
		XLibrApplication.mainStage.setScene(new Scene(root));
	}

	@FXML
	public void LoginSwitch(ActionEvent _actionEvent) throws Exception {
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-view.fxml")));
		XLibrApplication.mainStage.setTitle("XLibr Login");
		XLibrApplication.mainStage.setScene(new Scene(root));
	}

	private void OpenMainStage() throws Exception {
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
		XLibrApplication.mainStage.setResizable(true);
		XLibrApplication.mainStage.setTitle("XLibr");
		XLibrApplication.mainStage.setMinWidth(1000);
		XLibrApplication.mainStage.setMinHeight(600);
		XLibrApplication.mainStage.setScene(new Scene(root));
	}

	private boolean Login(String _username, String _password) {
		if (_username.equals("") || _password.equals(""))
			return false;

		Log.getInstance();
		s_Request = new ClientRequest();
		s_Request.value = LOGIN;

		UserData userData = new UserData();

		userData.setUsername(_username);
		String hashedPass = XLibrApplication.GetHash(_password.getBytes(), "SHA-256");
		userData.setPassword(hashedPass);

		Log.INFO(userData.toString());


		try {
			s_Xlibrconnect = new XLibrConnect(XLibrApplication.IP, XLibrApplication.PORT);
			s_Xlibrconnect.SendObject(s_Request);
			s_Xlibrconnect.SendObject(userData);
		} catch (Exception e) {
			Log.CRITICAL("Unable to connect to server!");
			return false;
		}

		ListenerThread listener = new ListenerThread();
		listener.run();

		if(!allowLogin)
			return false;

		return true;
	}

	private boolean Signup(String username, String firstname, String lastname, String email, String address, String password) {
		return true;
	}
}
