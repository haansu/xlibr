package com.xlibrpkg;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

	public static final String s_RESET	= "\u001B[0m";
	public static final String s_RED	= "\u001B[31m";
	public static final String s_GREEN	= "\u001B[32m";
	public static final String s_YELLOW = "\u001B[33m";
	public static final String s_BLUE	= "\u001B[34m";
	public static final String s_PURPLE	= "\u001B[35m";
	public static final String s_CYAN	= "\u001B[36m";

	private static Log s_Instance = null;

	private static DateTimeFormatter format;

	private Log(String _formatText) {
		format = DateTimeFormatter.ofPattern(_formatText);
	}

	public static void INFO(String _text) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(format.format(time) + s_CYAN + "|INFO:\t\t" + s_RESET + _text);
	}

	public static void WARN(String _text) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(format.format(time) + s_YELLOW + "|WARNING:\t" + s_RESET + _text);
	}


	public static void ERROR(String _text) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(format.format(time) + s_RED + "|ERROR:\t\t" + s_RESET + _text);
	}

	public static void CRITICAL(String _text) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(format.format(time) + s_PURPLE + "|CRITICAL:\t" + s_RESET + _text);
	}

	public static void SUCCESS(String _text) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(format.format(time) + s_GREEN + "|SUCCESS:\t" + s_RESET + _text);
	}

	public static void NOTE(String _text) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(format.format(time) + s_BLUE + "|NOTE:\t\t" + s_RESET + _text);
	}

	public static Log getInstance() {
		if (s_Instance == null)
			s_Instance = new Log("dd/MM/yyyy HH:mm:ss");

		return s_Instance;
	}

}
