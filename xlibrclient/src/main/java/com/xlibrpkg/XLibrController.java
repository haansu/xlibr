package com.xlibrpkg;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.xlibrpkg.ClientRequest.RequestType.*;
import static com.xlibrpkg.XLibrGlobals.*;
import static java.lang.Thread.sleep;

public class XLibrController {

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
	public TableColumn<BookData, String> c_title;
	@FXML
	public TableColumn<BookData, String> c_author;
	@FXML
	public TableColumn<BookData, String> c_publisher;
	@FXML
	public TableColumn<BookData, Integer> c_release_year;

	@FXML
	private TableView<BookData> myBooksTable;

	@FXML
	public TableColumn<BookData, String> m_title;
	@FXML
	public TableColumn<BookData, String> m_author;
	@FXML
	public TableColumn<BookData, String> m_publisher;
	@FXML
	public TableColumn<BookData, Integer> m_release_year;

	@FXML
	private TableView<BookData> booksTable;

	@FXML
	private TabPane mainTabPane;
	@FXML
	private Tab lentBooksTab;
	@FXML
	private Tab addBookTab;
	@FXML
	private Tab removeBookTab;
	@FXML
	private Tab manageUsersTab;

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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
		Parent root = loader.load();
		XLibrController controller = loader.getController();
		XLibrApplication.mainStage.setResizable(true);
		XLibrApplication.mainStage.setTitle("XLibr");
		XLibrApplication.mainStage.setMinWidth(1000);
		XLibrApplication.mainStage.setMinHeight(600);
		XLibrApplication.mainStage.setScene(new Scene(root));

		s_Request.value = TRANSFERBOOKS;
		s_Xlibrconnect.SendObject(s_Request);

		ListenerThread listener = new ListenerThread();
		listener.run();

		controller.DisplayBooks();
		controller.RemoveAdminTabs();
	}

	@FXML
	public void RemoveAdminTabs() {
		if (s_UserRole == 0) {
			mainTabPane.getTabs().remove(lentBooksTab);
			mainTabPane.getTabs().remove(addBookTab);
			mainTabPane.getTabs().remove(removeBookTab);
			mainTabPane.getTabs().remove(manageUsersTab);
		}
	}

	@FXML
	public void DisplayBooks() {
		c_title.setCellValueFactory(new PropertyValueFactory<BookData, String>("title"));
		c_author.setCellValueFactory(new PropertyValueFactory<BookData, String>("author"));
		c_publisher.setCellValueFactory(new PropertyValueFactory<BookData, String>("publisher"));
		c_release_year.setCellValueFactory(new PropertyValueFactory<BookData, Integer>("releaseYear"));

		ObservableList<BookData> books = FXCollections.observableArrayList();
		for (BookData elem : s_BookList) {
			books.add(elem);
		}
		booksTable.setItems(books);

		m_title.setCellValueFactory(new PropertyValueFactory<BookData, String>("title"));
		m_author.setCellValueFactory(new PropertyValueFactory<BookData, String>("author"));
		m_publisher.setCellValueFactory(new PropertyValueFactory<BookData, String>("publisher"));
		m_release_year.setCellValueFactory(new PropertyValueFactory<BookData, Integer>("releaseYear"));

		ObservableList<BookData> myBooks = FXCollections.observableArrayList();
		for (BookData elem : s_MyBooks) {
			myBooks.add(elem);
		}
		myBooksTable.setItems(myBooks);
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
				s_Xlibrconnect = new XLibrConnect(IP, PORT);
			s_Xlibrconnect.SendObject(s_Request);
			s_Xlibrconnect.SendObject(userData);
		} catch (Exception e) {
			Log.CRITICAL("Unable to connect to server!");
			return false;
		}

		ListenerThread listener = new ListenerThread();
		listener.run();

		return allowLogin;
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
				s_Xlibrconnect = new XLibrConnect(IP, PORT);
			s_Xlibrconnect.SendObject(s_Request);
			s_Xlibrconnect.SendObject(userData);
		} catch (Exception e) {
			Log.CRITICAL("Unable to connect to server!");
			return false;
		}

		ListenerThread listener = new ListenerThread();
		listener.run();

		return allowSignup;
	}
}
