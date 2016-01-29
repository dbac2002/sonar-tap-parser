package com.cetrea.genericTestConverter.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "testCase")
public class GenericTestCase {
	@XmlAttribute
	public String name;
	@XmlAttribute
	private String duration;
	@XmlElement
	public GenericTestFailure failure;

	public void setName(String name) {
		this.name = name;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public void setFailure(GenericTestFailure failure) {
		this.failure = failure;
	}

	@Override
	public String toString() {
		return "GenericTestCase [name=" + name + ", duration=" + duration + ", failure=" + failure + "]";
	}
}
