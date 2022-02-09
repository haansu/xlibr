package com.xlibrpkg;

import java.io.*;
import java.net.Socket;

public class ListenerThread extends Thread {

	public boolean exit = false;

	public void run() {
		Log.getInstance();
		Log.INFO("Thread running");

		while (XLibrConnect.s_Comms == true) {
			if (exit)
				return;

			try {
				Log.NOTE("Receiving data!");

				if (!DataRouter(XLibrConnect.s_Socket)) {
					Log.INFO("Connection closed!");
					break;
				}

				XLibrConnect.s_Socket.getOutputStream().flush();
			} catch (IOException e) {
				if (!XLibrConnect.s_Socket.isClosed()) {
					Log.NOTE("Connection closed!");
					break;
				}
			}
		}
	}

	public void kill() {
		exit = true;
	}

	public <T> T ReceiveObject() {
		T receivedObj;
		try {
			XLibrConnect.s_ObjOutputStr.reset();
		} catch (IOException e){
			e.printStackTrace();
		}
		try {
			receivedObj = (T) XLibrConnect.s_ObjInputStr.readObject();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		return receivedObj;
	}

	public boolean DataRouter(Socket socket) {
		if (socket.isClosed())
			return false;

		var receivedRequest = ReceiveObject();
		if (receivedRequest instanceof UserData)
			Log.SUCCESS("Object received: UserData");

		kill();
		return true;
	}
}
