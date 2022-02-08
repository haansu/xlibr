package com.xlibrpkg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Objects;

public class XLibrController {
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;

	@FXML
	public void LoginButton(ActionEvent actionEvent) {
		String userText = usernameField.getText();
		String passText = passwordField.getText();
		Log.INFO(userText);
		Log.INFO(passText);
		try {
			OpenMainStage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void SignupSwitch(ActionEvent actionEvent) throws Exception {
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signup-view.fxml")));
		XLibrApplication.mainStage.setTitle("XLibr Signup");
		XLibrApplication.mainStage.setScene(new Scene(root));
	}

	@FXML
	public void LoginSwitch(ActionEvent actionEvent) throws Exception {
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

	private boolean Login() {


		return true;
	}

	private boolean Signup() {

		return true;
	}
}
