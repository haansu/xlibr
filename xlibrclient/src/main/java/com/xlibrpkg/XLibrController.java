package com.xlibrpkg;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.util.Objects;

import static com.xlibrpkg.ClientRequest.RequestType.*;
import static com.xlibrpkg.XLibrGlobals.*;
import static java.lang.Thread.sleep;

public class XLibrController {

	XLibrController controller;

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
	private TextField add_book_title;
	@FXML
	private TextField add_book_author;
	@FXML
	private TextField add_book_publisher;
	@FXML
	private TextField add_book_synopsis;
	@FXML
	private TextField add_book_release;
	@FXML
	private Text add_book_text;


	@FXML
	public void LoginButton(ActionEvent _actionEvent) {
		String userText = usernameField.getText();
		String passText = passwordField.getText();
		Log.INFO("Username - " + userText);
		Log.INFO("Password - " + XLibrApplication.GetHash(passText.getBytes(), "Sha-256"));

		// Opens main stage if login is valid
		if (Login(userText, passText))
			try {
				OpenMainStage();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@FXML
	public void SignupButton(ActionEvent _actionEvent) {
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
		// Switches scene to the signup scene
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signup-view.fxml")));
		XLibrApplication.mainStage.setTitle("XLibr Signup");
		XLibrApplication.mainStage.setScene(new Scene(root));
	}

	@FXML
	public void LoginSwitch(ActionEvent _actionEvent) throws Exception {
		// Switches scene to the login scene
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-view.fxml")));
		XLibrApplication.mainStage.setTitle("XLibr Login");
		XLibrApplication.mainStage.setScene(new Scene(root));
	}

	private void OpenMainStage() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
		Parent root = loader.load();
		controller = loader.getController();
		XLibrApplication.mainStage.setResizable(true);
		XLibrApplication.mainStage.setTitle("XLibr");
		XLibrApplication.mainStage.setMinWidth(1000);
		XLibrApplication.mainStage.setMinHeight(600);
		XLibrApplication.mainStage.setScene(new Scene(root));

		// Requests data for books to populate the tables
		s_Request.value = TRANSFERBOOKS;
		s_Xlibrconnect.SendObject(s_Request);

		// Started listener to listen to commands from the server
		ListenerThread listener = new ListenerThread();
		listener.start();

		// Don't know why, but it works!
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
		c_title.setCellValueFactory(new PropertyValueFactory<>("title"));
		c_author.setCellValueFactory(new PropertyValueFactory<>("author"));
		c_publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		c_release_year.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));

		ObservableList<BookData> books = FXCollections.observableArrayList();
		books.addAll(s_BookList);

		booksTable.setItems(books);

		m_title.setCellValueFactory(new PropertyValueFactory<>("title"));
		m_author.setCellValueFactory(new PropertyValueFactory<>("author"));
		m_publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		m_release_year.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));

		ObservableList<BookData> myBooks = FXCollections.observableArrayList();
		myBooks.addAll(s_MyBooks);

		myBooksTable.setItems(myBooks);
	}

	@FXML
	public void RefreshMyBooks() {
		ObservableList<BookData> myBooks = myBooksTable.getItems();
		myBooks.clear();
		myBooks.addAll(s_MyBooks);
		myBooksTable.setItems(myBooks);
	}

	@FXML
	public void RefreshBooks() {
		ObservableList<BookData> books = booksTable.getItems();
		books.clear();
		books.addAll(s_BookList);
		myBooksTable.setItems(books);
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

		// Starts a new connection only if it has not connected to the server in current session
		if (!XLibrConnect.HadConnection())
			s_Xlibrconnect = new XLibrConnect(IP, PORT);


		s_Xlibrconnect.SendObject(s_Request);
		s_Xlibrconnect.SendObject(userData);

		ListenerThread listener = new ListenerThread();
		listener.start();


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
			if (!XLibrConnect.HadConnection())
				s_Xlibrconnect = new XLibrConnect(IP, PORT);
			s_Xlibrconnect.SendObject(s_Request);
			s_Xlibrconnect.SendObject(userData);
		} catch (Exception e) {
			Log.CRITICAL("Unable to connect to server!");
			return false;
		}

		ListenerThread listener = new ListenerThread();
		listener.start();

		return allowSignup;
	}

	@FXML
	private void BorrowBook(ActionEvent _actionEvent) {
		// Add book to user's library
		BookData selectedBook = new BookData();
		try {
			selectedBook = booksTable.getSelectionModel().getSelectedItem();
		} catch (Exception e) {
			Log.WARN("There was no selection!");
		}
		Log.INFO(selectedBook.toString());

		// Sends required data to the server
		s_Request.value = BORROW;
		s_Xlibrconnect.SendObject(s_Request);
		s_Xlibrconnect.SendObject(selectedBook.getId());

		ListenerThread listener = new ListenerThread();
		listener.start();

		RefreshMyBooks();
	}

	@FXML
	public void AddBook(ActionEvent _actionEvent) {
		BookData book = new BookData();
		book.setTitle(add_book_title.getText());
		book.setAuthor(add_book_author.getText());
		book.setPublisher(add_book_publisher.getText());
		book.setSynopsis(add_book_synopsis.getText());
		book.setReleaseYear(Integer.parseInt(add_book_release.getText()));

		s_Request.value = ADDBOOK;
		s_Xlibrconnect.SendObject(s_Request);
		s_Xlibrconnect.SendObject(book);

		ListenerThread listener = new ListenerThread();
		listener.start();

		RefreshBooks();

		add_book_text.setText("Book Added");
		Log.NOTE("Book Added");

		new Thread(() -> {
			try {
				sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			add_book_text.setText("");
		}).start();
	}

	@FXML
	public void RemoveBook(ActionEvent _actionEvent) {

	}
}
