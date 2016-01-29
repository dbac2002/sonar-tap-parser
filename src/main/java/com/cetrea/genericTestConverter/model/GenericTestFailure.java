package com.cetrea.genericTestConverter.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "failure")
public class GenericTestFailure {
	@XmlAttribute
	public String message = "";

	@XmlValue
	public String stackTrace = "";

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public void addToStackTrace(String line) {
		stackTrace += line;
		stackTrace += '\n';
	}

	@Override
	public String toString() {
		return "GenericTestFailure [message=" + message + ", stackTrace=" + stackTrace + "]";
	}
}
