package com.xlibrserver;

import java.awt.print.Book;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xlibrpkg.BookData;
import com.xlibrpkg.Log;

public class Main {

	public static int s_Role;
	public static final int PORT = 23313;

	public static void main(String[] args) {
		Log.getInstance();
		Log.INFO("Starting...");

		DBConnect.getInstance();
		List<BookData> x = DBConnect.GetBooks();

		for (BookData elem : x) {
			Log.WARN(elem.toString());
		}

		ExecutorService executor = Executors.newCachedThreadPool();

		try {
			XLibrconnect xlibrconnect = new XLibrconnect(PORT);
			Log.SUCCESS("On-line!");
			executor.submit(xlibrconnect);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
