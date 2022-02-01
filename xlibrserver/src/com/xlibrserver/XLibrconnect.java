package com.xlibrserver;

import java.io.*;
import java.lang.invoke.VarHandle;
import java.net.*;

import com.mysql.cj.xdevapi.Client;
import com.xlibrpkg.UserData;
import com.xlibrpkg.ClientRequest;

public class XLibrconnect implements Runnable {

	private ServerSocket serverSocket;

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
		System.out.println("Waiting for connection!");

		while(!Thread.interrupted()) {
			try (Socket socket = serverSocket.accept()) {
				inputStrRd = new InputStreamReader(socket.getInputStream());
				buffReader = new BufferedReader(inputStrRd);

				buffPrint = buffReader.readLine();
				System.out.println("Client: " + buffPrint);

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
			System.out.print("Client request: ");
			ClientRequest request = (ClientRequest) receivedRequest;
			if (request.value == ClientRequest.RequestType.LOGIN)
				System.out.print("Login;\n");
			else if (request.value == ClientRequest.RequestType.SIGNUP)
				System.out.print("Signup;\n");
		}

		var receivedObject = ReceiveObject(socket);

		if (receivedObject instanceof UserData) {
			System.out.println("Received 'UserData': ");
			userData = (UserData) receivedObject;
			receivedObject = null;
		}

		System.out.println(userData);
	}
}
