package com.xlibrpkg;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class XLibrconnect {
	String 				host;
	int					port;

	Socket				clientSocket;
	InetAddress			ip;

	PrintWriter 		printWr;
	InputStreamReader	inputStr;
	BufferedReader		buffReader;

	String				buffPrint;

	OutputStream		outputStr;
	ObjectOutputStream	objOutputStr;

	XLibrconnect(String _host, int _port) throws IOException {
		this.host = _host;
		this.port = _port;

		clientSocket = new Socket(this.host, this.port);

		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException exp) {
			exp.printStackTrace();
			Log.ERROR("Unable to get localhost ip address!");
		}

		printWr = new PrintWriter(clientSocket.getOutputStream());
		printWr.println("Connected!");
		printWr.flush();

		inputStr	= new InputStreamReader(clientSocket.getInputStream());
		buffReader	= new BufferedReader(inputStr);

		buffPrint	= buffReader.readLine();
		Log.INFO("Server - " + buffPrint);

	}

	public <T> void SendObject(T _object) {
		try {
			outputStr = clientSocket.getOutputStream();
			objOutputStr = new ObjectOutputStream(outputStr);

			Log.INFO("Sending object to server!");
			objOutputStr.writeObject(_object);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
