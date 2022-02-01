package com.xlibrserver;

import java.io.*;
import java.net.*;
import com.xlibrpkg.UserData;

public class XLibrconnect implements Runnable {

	private ServerSocket		serverSocket;

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

		} catch (IOException exp) {
			exp.printStackTrace();
			return null;
		} catch (ClassNotFoundException exp) {
			exp.printStackTrace();
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
		System.out.println("Accepting connection.");

		while(!Thread.interrupted()) {
			try (Socket socket = serverSocket.accept()) {
				inputStrRd = new InputStreamReader(socket.getInputStream());
				buffReader = new BufferedReader(inputStrRd);

				buffPrint = buffReader.readLine();
				System.out.println("Client: " + buffPrint);

				printWr = new PrintWriter(socket.getOutputStream());
				printWr.println("Connected!");
				printWr.flush();

				PrintReceivedObject(socket);
			} catch (SocketTimeoutException e) { }
		}
	}

	public void PrintReceivedObject(Socket socket) {
		var receivedObject = ReceiveObject(socket);

		if (receivedObject instanceof UserData) {
			System.out.println("Received 'UserData' object");
			userData = (UserData) receivedObject;
		}

		System.out.println(userData);

	}
}
