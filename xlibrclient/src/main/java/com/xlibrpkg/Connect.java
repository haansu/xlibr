package com.xlibrpkg;

import java.io.IOException;
import java.net.Socket;

import static com.xlibrpkg.XLibrGlobals.*;
import static java.lang.Thread.sleep;

public class Connect {

	public void start() {
		try {
			while (true) {
				s_Socket = new Socket(IP, PORT);
				if (s_Socket.isClosed() || s_Socket != null)
					break;
				sleep(150);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
