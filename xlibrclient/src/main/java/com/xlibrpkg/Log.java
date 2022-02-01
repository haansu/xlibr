package com.xlibrpkg;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

	public static final String RESET	= "\u001B[0m";
	public static final String RED		= "\u001B[31m";
	public static final String GREEN	= "\u001B[32m";
	public static final String YELLOW	= "\u001B[33m";
	public static final String BLUE		= "\u001B[34m";
	public static final String PURPLE	= "\u001B[35m";
	public static final String CYAN		= "\u001B[36m";

	private static Log instance = null;

	private static DateTimeFormatter format;

	public Log(String formatText) {
		format = DateTimeFormatter.ofPattern(formatText);
	}

	public static void INFO(String text) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(format.format(time) + CYAN + "  INFO:\t\t" + RESET + text);
	}

	public static void WARN(String text) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(format.format(time) + YELLOW + "  WARNING:\t" + RESET + text);
	}


	public static void ERROR(String text) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(format.format(time) + RED + "  ERROR:\t\t" + RESET + text);
	}

	public static void CRITICAL(String text) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(format.format(time) + PURPLE + "  CRITICAL:\t" + RESET + text);
	}

	public static void SUCCESS(String text) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(format.format(time) + GREEN + "  SUCCESS:\t" + RESET + text);
	}

	public static void NOTE(String text) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(format.format(time) + BLUE + "  NOTE:\t\t" + RESET + text);
	}

	public static Log getInstance() {
		if (instance == null)
			instance = new Log("dd/MM/yyyy HH:mm:ss");

		return instance;
	}

}
