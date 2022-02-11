package com.xlibrpkg;

import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class XLibrGlobals {

	static public XLibrConnect s_Xlibrconnect;
	static public ClientRequest s_Request;
	static public boolean allowLogin = false;
	static public boolean allowSignup = false;
	static public List<BookData> s_BookList;

	static public String IP = "192.168.0.174";
	static public int PORT = 23313;

	static public Socket s_Socket;

	static public ObjectOutputStream s_ObjOutputStr;
	static public ObjectInputStream s_ObjInputStr;

}
