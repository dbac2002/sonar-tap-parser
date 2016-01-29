package com.cetrea.genericTestConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.cetrea.genericTestConverter.model.GenericTest;
import com.cetrea.genericTestConverter.model.GenericTestCase;
import com.cetrea.genericTestConverter.model.GenericTestFailure;
import com.cetrea.genericTestConverter.model.GenericTestFile;

public class GenericTestTapParser {
	private static final String LOG = "Log";

	private static final String NOT_OK = "not ok";

	private static final String OK = "ok";

	private static final String STACK = "stack";

	private static final String MESSAGE = "message";

	private static final String NOT_SET = "NOT_SET";

	private static final String SPEC = "SPEC";

	private Map<String, GenericTestFile> testCasesByFiles = new HashMap<>();

	private GenericTest genericTest = new GenericTest();

	private GenericTestTapParser() {
	}

	public static GenericTest parseTapFile(InputStream is) throws IOException {
		return new GenericTestTapParser().doParse(is);
	}

	private GenericTest doParse(InputStream is) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			GenericTestCase testCase = new GenericTestCase();
			GenericTestFailure testFailure = new GenericTestFailure();
			String line = "";
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith(OK) || line.startsWith(NOT_OK)) {
					testCase = new GenericTestCase();
					setTestCaseProperties(line, testCase);
				}
				else if (line.startsWith(MESSAGE)) {
					testFailure.setMessage(reader.readLine().trim());
				}
				else if (line.startsWith(STACK)) {
					line = readStackTrace(reader, testFailure);
					testCase.setFailure(testFailure);
					testFailure = new GenericTestFailure();
				}
				else if (line.startsWith(SPEC)) {
					saveSpecFile(line, testCase);
				}
			}
		}
		addAllSetSpecsToTest();
		return genericTest;
	}

	private void addAllSetSpecsToTest() {
		testCasesByFiles.entrySet().stream().filter(k -> !k.getKey().equals(NOT_SET))
				.forEach(e -> genericTest.addFile(e.getValue()));
	}

	private void setTestCaseProperties(String line, GenericTestCase testCase) {
		testCase.setDuration("0");
		testCase.setName(line.substring(line.indexOf('-') + 2));
	}

	private String readStackTrace(BufferedReader reader, GenericTestFailure testFailure) throws IOException {
		String line = reader.readLine();
		while (!line.startsWith(LOG)) {
			testFailure.addToStackTrace(line.trim());
			line = reader.readLine().trim();
		}
		return line;
	}

	private void saveSpecFile(String line, GenericTestCase testCase) {
		String spec = line.substring(line.indexOf(':') + 2);

		GenericTestFile genericTestFile = testCasesByFiles.getOrDefault(spec, new GenericTestFile());
		genericTestFile.setPath(spec);
		genericTestFile.addTestCase(testCase);
		testCasesByFiles.put(spec, genericTestFile);
	}
}
