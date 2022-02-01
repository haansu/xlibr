package com.xlibrpkg;

public class ClientRequest {

	public enum RequestType{
		LOGIN, SIGNUP
	}

	RequestType requestType;

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

}
