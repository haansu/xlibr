package com.xlibrpkg;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

import static com.xlibrpkg.XLibrGlobals.*;

public class XLibrConnect extends Thread {

	static public boolean s_Comms = false;
	static private boolean s_HadConnection = false;


	XLibrConnect(String _host, int _port) {
		// Connects to the server
		try {
			if (s_Socket == null)
				s_Socket = new Socket(_host, _port);

			s_HadConnection = true;

			s_ObjOutputStr = new ObjectOutputStream(s_Socket.getOutputStream());
			s_ObjInputStr = new ObjectInputStream(s_Socket.getInputStream());
		} catch (ConnectException e) {
			Log.ERROR("Connection refused!");
		} catch (SocketException e) {
			Log.ERROR("Connection reset!");
		} catch (IOException e) {
			e.printStackTrace();
		}

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
