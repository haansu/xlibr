package com.xlibrpkg;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Objects;

import static com.xlibrpkg.ClientRequest.RequestType.*;

public class XLibrController  {

	static public XLibrConnect s_Xlibrconnect;
	static public ClientRequest s_Request;
	static public boolean allowLogin = false;
	static public boolean allowSignup = false;
	static public List<BookData> s_BookList;


	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;

	@FXML
	private TextField sign_username;
	@FXML
	private TextField sign_firstname;
	@FXML
	private TextField sign_lastname;
	@FXML
	private PasswordField sign_password;
	@FXML
	private PasswordField sign_repassword;
	@FXML
	private TextField sign_email;
	@FXML
	private TextField sign_address;


	@FXML
	static public TableView bookTable;
	@FXML
	static public TableColumn<BookData, String> c1;
	@FXML
	static public TableColumn<BookData, String> c2;
	@FXML
	static public TableColumn<BookData, String> c3;
	@FXML
	static public TableColumn<BookData, Integer> c4;


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
	public void SignupButton(ActionEvent actionEvent) {
		String username = sign_username.getText();
		String firstname = sign_firstname.getText();
		String lastname =  sign_lastname.getText();
		String password =  sign_password.getText();
		String repassword =  sign_repassword.getText();
		String email = sign_email.getText();
		String address = sign_address.getText();

		if (!password.equals(repassword))
			return;

		Log.INFO("Username - " + username);
		Log.INFO("Password - " + XLibrApplication.GetHash(password.getBytes(), "Sha-256"));
		if (Signup(username, firstname, lastname, password, email, address))
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

		s_Request.value = TRANSFERBOOKS;
		s_Xlibrconnect.SendObject(s_Request);

		ListenerThread listener = new ListenerThread();
		listener.run();
	}

	@FXML
	static public void DisplayBooks() {
		c1 = new TableColumn<>();
		c2 = new TableColumn<>();
		c3 = new TableColumn<>();
		c4 = new TableColumn<>();
		c1.setCellValueFactory(new PropertyValueFactory<BookData, String>("title"));
		c2.setCellValueFactory(new PropertyValueFactory<BookData, String>("author"));
		c3.setCellValueFactory(new PropertyValueFactory<BookData, String>("publisher"));
		c4.setCellValueFactory(new PropertyValueFactory<BookData, Integer>("releaseYear"));

		ObservableList<BookData> obsList = FXCollections.observableArrayList(s_BookList);

		//bookTable.setItems(obsList);
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
			if (!s_Xlibrconnect.HadConnection())
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

	private boolean Signup(String _username, String _firstname, String _lastname, String _email, String _address, String _password) {
		if (_username.equals("") || _password.equals(""))
			return false;

		Log.getInstance();
		s_Request = new ClientRequest();
		s_Request.value = SIGNUP;

		UserData userData = new UserData();

		userData.setUsername(_username);
		String hashedPass = XLibrApplication.GetHash(_password.getBytes(), "SHA-256");
		userData.setPassword(hashedPass);
		userData.setFirstname(_firstname);
		userData.setLastname(_lastname);
		userData.setEmail(_email);
		userData.setAddress(_address);

		Log.INFO(userData.toString());


		try {
			if (!s_Xlibrconnect.HadConnection())
				s_Xlibrconnect = new XLibrConnect(XLibrApplication.IP, XLibrApplication.PORT);
			s_Xlibrconnect.SendObject(s_Request);
			s_Xlibrconnect.SendObject(userData);
		} catch (Exception e) {
			Log.CRITICAL("Unable to connect to server!");
			return false;
		}

		ListenerThread listener = new ListenerThread();
		listener.run();

		if(!allowSignup)
			return false;

		return true;
	}
}
