package com.xlibrpkg;

import java.io.*;
import java.net.Socket;

import static com.xlibrpkg.XLibrGlobals.*;

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

				if (!DataRouter(s_Socket)) {
					Log.INFO("Connection closed!");
					break;
				}

				s_Socket.getOutputStream().flush();
			} catch (IOException e) {
				if (!s_Socket.isClosed()) {
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
			s_ObjOutputStr.reset();
		} catch (IOException e){
			e.printStackTrace();
		}
		try {
			receivedObj = (T) s_ObjInputStr.readObject();

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
		if (receivedRequest instanceof ClientRequest) {
			ClientRequest req = (ClientRequest) receivedRequest;
			switch (req.value) {
				case LOGIN: {
					boolean allow = ReceiveObject();
					if (allow)
						allowLogin = true;
					else
						allowLogin = false;

					break;
				}

				case SIGNUP: {
					boolean allow = ReceiveObject();
					if (allow)
						allowSignup = true;
					else
						allowSignup = false;

					break;
				}

				case TRANSFERBOOKS:
					s_BookList = ReceiveObject();
					//XLibrController.DisplayBooks();
					for (BookData elem : s_BookList) {
						Log.WARN(elem.toString());
					}
					break;
			}

		}

		kill();
		return true;
	}
}
