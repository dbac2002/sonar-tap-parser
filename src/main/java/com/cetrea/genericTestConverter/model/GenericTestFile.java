package com.cetrea.genericTestConverter.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "file")
public class GenericTestFile {
	@XmlElement
	public Collection<GenericTestCase> testCases = new ArrayList<>();
	@XmlAttribute
	public String path;

	public void setPath(String path) {
		this.path = path;
	}

	public void setTestCases(Collection<GenericTestCase> testCases) {
		this.testCases = testCases;
	}

	public void addTestCase(GenericTestCase testCase) {
		testCases.add(testCase);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GenericTestFile other = (GenericTestFile) obj;
		if (path == null) {
			if (other.path != null) {
				return false;
			}
		}
		else if (!path.equals(other.path)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "GenericTestFile [testCases=" + testCases + ", path=" + path + "]";
	}
}
