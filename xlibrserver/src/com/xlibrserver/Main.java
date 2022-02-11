package com.xlibrserver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xlibrpkg.Log;

public class Main {

	public static final int PORT = 23313;

	public static void main(String[] args) {
		Log.getInstance();
		Log.INFO("Starting...");

		DBConnect.getInstance();

		// Creates a thread pool
		ExecutorService executor = Executors.newCachedThreadPool();

		try {
			XLibrconnect xlibrconnect = new XLibrconnect(PORT);
			Log.SUCCESS("On-line!");
			// Every new connection is sumbited to the executor
			executor.submit(xlibrconnect);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
