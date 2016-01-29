package com.cetrea.genericTestConverter.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "unitTest")
public class GenericTest {
	@XmlAttribute
	private String version = "1";

	@XmlElement
	public List<GenericTestFile> files = new ArrayList<>();

	public void setFiles(List<GenericTestFile> files) {
		this.files = files;
	}

	public void addFile(GenericTestFile testFile) {
		files.add(testFile);
	}

	@Override
	public String toString() {
		return "GenericTest [version=" + version + ", files=" + files + "]";
	}
}
