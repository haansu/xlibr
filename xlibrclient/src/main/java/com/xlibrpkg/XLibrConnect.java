package com.xlibrpkg;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class XLibrConnect extends Thread {
	static public Socket s_Socket;

	InetAddress ip;

	PrintWriter printWr;
	InputStreamReader inputStrRd;
	BufferedReader buffReader;

	String buffPrint;

	static public ObjectOutputStream s_ObjOutputStr;
	static public ObjectInputStream s_ObjInputStr;

	static public boolean s_Comms = false;


	XLibrConnect(String _host, int _port) throws IOException {

		s_Socket = new Socket(_host, _port);

		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			Log.ERROR("Unable to get localhost ip address!");
		}

		printWr = new PrintWriter(s_Socket.getOutputStream());
		printWr.println("Connected!");
		printWr.flush();

		inputStrRd = new InputStreamReader(s_Socket.getInputStream());
		buffReader	= new BufferedReader(inputStrRd);

		buffPrint	= buffReader.readLine();
		Log.INFO("Server - " + buffPrint);

		s_ObjOutputStr = new ObjectOutputStream(s_Socket.getOutputStream());
		s_ObjInputStr = new ObjectInputStream(s_Socket.getInputStream());

		s_Comms = true;
	}

	public <T> void SendObject(T _object) {
		try {
			Log.INFO("Sending object to server!");
			s_ObjOutputStr.writeObject(_object);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
