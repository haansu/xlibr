package com.xlibrpkg;

import java.io.*;
import java.net.Socket;
import java.util.List;

import static com.xlibrpkg.ClientRequest.RequestType.LOGIN;

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
		if (receivedRequest instanceof ClientRequest) {
			ClientRequest req = (ClientRequest) receivedRequest;
			switch (req.value) {
				case LOGIN: {
					boolean allow = ReceiveObject();
					if (allow)
						XLibrController.allowLogin = true;
					else
						XLibrController.allowLogin = false;

					break;
				}

				case SIGNUP: {
					boolean allow = ReceiveObject();
					if (allow)
						XLibrController.allowSignup = true;
					else
						XLibrController.allowSignup = false;

					break;
				}

				case TRANSFERBOOKS:
					XLibrController.s_BookList = ReceiveObject();
					XLibrController.DisplayBooks();
					for (BookData elem : XLibrController.s_BookList) {
						Log.WARN(elem.toString());
					}
					break;
			}

		}

		kill();
		return true;
	}
}
