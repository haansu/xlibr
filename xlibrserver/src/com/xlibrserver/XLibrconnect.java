package com.xlibrserver;

import java.io.*;
import java.net.*;

import com.xlibrpkg.Log;
import com.xlibrpkg.UserData;
import com.xlibrpkg.ClientRequest;

public class XLibrconnect implements Runnable {

	ServerSocket		serverSocket;

	InputStreamReader	inputStrRd;
	BufferedReader		buffReader;

	String				buffPrint;

	PrintWriter			printWr;

	InputStream 		inputStr;
	ObjectInputStream	objInputStr;

	int port;
	UserData userData;

	XLibrconnect(int _port) {
		try {

			serverSocket = new ServerSocket(_port);
			serverSocket.setSoTimeout(250);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public <T> T ReceiveObject(Socket socket) {
		T receivedObj;

		try {
			inputStr = socket.getInputStream();
			objInputStr = new ObjectInputStream(inputStr);
			receivedObj = (T) objInputStr.readObject();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		return receivedObj;
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
			try (Socket socket = serverSocket.accept()) {
				inputStrRd = new InputStreamReader(socket.getInputStream());
				buffReader = new BufferedReader(inputStrRd);

				buffPrint = buffReader.readLine();
				Log.INFO("Client - " + buffPrint);

				printWr = new PrintWriter(socket.getOutputStream());
				printWr.println("Connected!");
				printWr.flush();

				DataRouter(socket);
			} catch (SocketTimeoutException e) { }
		}
	}

	public void DataRouter(Socket socket) {
		var receivedRequest = ReceiveObject(socket);

		if (receivedRequest instanceof ClientRequest) {
			ClientRequest request = (ClientRequest) receivedRequest;

			switch (request.value) {
				case LOGIN: {
					Log.INFO(socket.getInetAddress().toString().substring(1) + " requested LOGIN!");

					var receivedData = ReceiveObject(socket);
					UserData loginData = (UserData) receivedData;
					Login(loginData);

					break;
				}

				case SIGNUP: {
					Log.INFO(socket.getInetAddress().toString().substring(1) + " requested SIGNUP!");

					var receivedData = ReceiveObject(socket);
					UserData signupData = (UserData) receivedData;
					SignUp(signupData);

					break;
				}

			}
		}

	}

	private void Login(UserData loginData) {

	}

	private void SignUp(UserData signupData) {

	}
}
