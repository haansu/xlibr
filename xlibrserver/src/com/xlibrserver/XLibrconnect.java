package com.xlibrserver;

import java.io.*;
import java.net.*;
import java.util.List;

import com.mysql.cj.xdevapi.Client;
import com.xlibrpkg.BookData;
import com.xlibrpkg.Log;
import com.xlibrpkg.UserData;
import com.xlibrpkg.ClientRequest;

import static com.xlibrpkg.ClientRequest.RequestType.*;

public class XLibrconnect implements Runnable {

	ServerSocket		serverSocket;
	Socket				socket;
	InputStreamReader	inputStrRd;
	BufferedReader		buffReader;

	String				buffPrint;
	PrintWriter			printWr;

	ObjectInputStream	objInputStr;
	ObjectOutputStream	objOutputStr;

	private boolean kill = false;


	XLibrconnect(int _port) {
		try {
			serverSocket = new ServerSocket(_port);
			serverSocket.setSoTimeout(5000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public <T> T ReceiveObject() {
		T receivedObj;

		try {
			receivedObj = (T) objInputStr.readObject();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		return receivedObj;
	}

	public <T> void SendObject(T _object) {
		try {
			Log.INFO("Sending object to client!");
			objOutputStr.writeObject(_object);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void accept() throws IOException {
		Log.INFO("Waiting for connection");

		while(!Thread.interrupted()) {
			try {

				socket = serverSocket.accept();

				objOutputStr = new ObjectOutputStream(socket.getOutputStream());
				objInputStr = new ObjectInputStream(socket.getInputStream());

				while (socket.isConnected()) {
					if (!DataRouter(socket)) {
						Log.SUCCESS("Connection closed!");
						break;
					}
				}

			} catch (SocketTimeoutException e) {
				if (!socket.isClosed())
					Log.NOTE("Ongoing connection with " + socket.getInetAddress().toString().substring(1) + "!");
			}
		}

	}

	public boolean DataRouter(Socket socket) {
		if (socket.isClosed())
			return false;

		var receivedRequest = ReceiveObject();

		if (receivedRequest instanceof ClientRequest) {
			ClientRequest request = (ClientRequest) receivedRequest;

			switch (request.value) {
				case LOGIN: {
					Log.INFO(socket.getInetAddress().toString().substring(1) + " requested LOGIN!");

					var receivedData = ReceiveObject();
					UserData loginData = (UserData) receivedData;

					try {
						objOutputStr.reset();
					} catch (IOException e){
						e.printStackTrace();
					}

					ClientRequest returnRequest = new ClientRequest();
					returnRequest.value = LOGIN;

					if (DBConnect.Login(loginData.getUsername(), loginData.getPassword())) {
						SendObject(returnRequest);
						SendObject(true);
					} else {
						SendObject(returnRequest);
						SendObject(false);
					}
					break;
				}

				case SIGNUP: {
					Log.INFO(socket.getInetAddress().toString().substring(1) + " requested SIGNUP!");

					var receivedData = ReceiveObject();
					UserData signupData = (UserData) receivedData;

					ClientRequest returnRequest = new ClientRequest();
					returnRequest.value = SIGNUP;

					if (DBConnect.Signup(signupData)) {
						SendObject(returnRequest);
						SendObject(true);
					} else {
						SendObject(returnRequest);
						SendObject(false);
					}

					break;
				}

				case TRANSFERBOOKS: {
					List<BookData> receivedBooks = DBConnect.GetBooks();

					ClientRequest returnRequest = new ClientRequest();
					returnRequest.value = TRANSFERBOOKS;

					SendObject(returnRequest);
					SendObject(receivedBooks);
					break;
				}

				case CLOSECONNECTION: {
					kill = true;
					try {
						socket.close();

					} catch	(IOException e) {
						e.printStackTrace();
					}
					return false;
				}
			}
		}
		return true;
	}
}
