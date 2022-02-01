package com.xlibrserver;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xlibrpkg.UserData;

public class Main {

	public static int PORT = 23313;

	public static void main(String[] args) throws IOException {

		System.out.println("Starting...");
		ExecutorService executor = Executors.newCachedThreadPool();

		try {
			XLibrconnect xlibrconnect = new XLibrconnect(PORT);
			System.out.println("Connected!");
			executor.submit(xlibrconnect);
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean running = true;

		String command;
		Scanner scanner = new Scanner(System.in);

		do {
			command = scanner.nextLine();
			if(command.toLowerCase(Locale.ROOT).equals("quit"))
				running = false;
		} while(running);

	}
}
