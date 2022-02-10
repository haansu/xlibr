package com.xlibrpkg;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class XLibrConnect extends Thread {
	static public Socket s_Socket;

	InetAddress ip;

	static public ObjectOutputStream s_ObjOutputStr;
	static public ObjectInputStream s_ObjInputStr;

	static public boolean s_Comms = false;
	static private boolean s_HadConnection = false;


	XLibrConnect(String _host, int _port) throws IOException {

		s_Socket = new Socket(_host, _port);
		s_HadConnection = true;

		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			Log.ERROR("Unable to get localhost ip address!");
		}

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

	static public boolean HadConnection() {
		return s_HadConnection;
	}

}
