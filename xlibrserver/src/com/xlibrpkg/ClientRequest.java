package com.xlibrpkg;

import java.io.Serializable;

public class ClientRequest implements Serializable {

	public enum RequestType{
		LOGIN, SIGNUP, CLOSECONNECTION,
		TRANSFERBOOKS
	}

	public RequestType value;

}
