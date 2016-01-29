package com.cetrea.genericTestConverter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.cetrea.genericTestConverter.model.GenericTest;
import com.cetrea.genericTestConverter.model.GenericTestCase;
import com.cetrea.genericTestConverter.model.GenericTestFailure;
import com.cetrea.genericTestConverter.model.GenericTestFile;
import com.google.common.collect.Iterables;

public class ParserTest {
	@Test
	public void parseOk() throws IOException {
		// @formatter:off
		String s = "ok 51 PhantomJS 1.9 - SOME_NAME\n"
					+ "---\n"
						+ "Log: |\n" +
							"WARNING: Tried to load angular more than once.\n" + 
							"SPEC: SUCCESS\n" +
						"...";
		GenericTest parse = parse(s);

		GenericTestFile first = Iterables.getFirst(parse.files, null);
		assertSuccess(first, 1);

		GenericTestCase testCase = Iterables.getFirst(first.testCases, null);
		assertThat(testCase.name).isEqualTo("SOME_NAME");
	}

	@Test
	public void parseMultipleOk() throws IOException {
		// @formatter:off
		String s = "ok 51 PhantomJS 1.9 - SOME_NAME_1\n"
					+ "---\n"
						+ "Log: |\n" +
							"WARNING: Tried to load angular more than once.\n" + 
							"SPEC: SUCCESS\n" +
						"...\n"+
					"ok 51 PhantomJS 1.9 - SOME_NAME_2\n"
					 + "---\n"
						+ "Log: |\n" +
							"WARNING: Tried to load angular more than once.\n" + 
							"SPEC: SUCCESS\n" +
						"...\n";
		// @formatter:on
		GenericTest parse = parse(s);

		assertThat(parse.files).hasSize(1);
		GenericTestFile genericTestFile = parse.files.get(0);
		assertSuccess(genericTestFile, 2);
	}

	@Test
	public void parseMultipleFailure() throws IOException {
		// @formatter:off
		String s = "not ok 51 PhantomJS 1.9 - SOME_NAME\n"
				+ "---\n"+
				 	"message: >\n"+
				 		"expected true to be false\n"+
				 	"stack: >\n"+
				 		"AssertionError: expected true to be false\n"+
				 		"at http://localhost:7357/static/desktop/test/lib/chai/chai.js:210\n"+
				 		"at http://localhost:7357/static/desktop/test/lib/chai/chai.js:593\n"+
				 		"at addProperty (http://localhost:7357/static/desktop/test/lib/chai/chai.js:4117)\n"+
				 		"at doAsserterAsyncAndAddThen (http://localhost:7357/static/desktop/test/lib/chai/chai-as-promised.js:298)\n"+
				 		"at http://localhost:7357/static/desktop/test/lib/chai/chai-as-promised.js:288\n"+
				 		"at http://localhost:7357/static/desktop/test/lib/chai/chai.js:5273\n"+
				 	"Log: |\n"+
				 		"WARNING: Tried to load angular more than once.\n"+
				 		"SPEC: FAILED\n"+
					"..."+
					"ok 51 PhantomJS 1.9 - SOME_NAME_2\n"
					 + "---\n"
						+ "Log: |\n" +
							"WARNING: Tried to load angular more than once.\n" + 
							"SPEC: SUCCESS\n" +
						"...\n";
		GenericTest parse = parse(s);

		assertThat(parse.files).hasSize(2);
		assertSuccess(parse.files.get(0), 1);
		assertFailure(parse.files.get(1).testCases.stream().findFirst().get());
	}

	@Test
	public void parseFailure() throws IOException {
		// @formatter:off
		String s = "not ok 51 PhantomJS 1.9 - SOME_NAME\n"
				+ "---\n"+
				 	"message: >\n"+
				 		"expected true to be false\n"+
				 	"stack: >\n"+
				 		"AssertionError: expected true to be false\n"+
				 		"at http://localhost:7357/static/desktop/test/lib/chai/chai.js:210\n"+
				 		"at http://localhost:7357/static/desktop/test/lib/chai/chai.js:593\n"+
				 		"at addProperty (http://localhost:7357/static/desktop/test/lib/chai/chai.js:4117)\n"+
				 		"at doAsserterAsyncAndAddThen (http://localhost:7357/static/desktop/test/lib/chai/chai-as-promised.js:298)\n"+
				 		"at http://localhost:7357/static/desktop/test/lib/chai/chai-as-promised.js:288\n"+
				 		"at http://localhost:7357/static/desktop/test/lib/chai/chai.js:5273\n"+
				 	"Log: |\n"+
				 		"WARNING: Tried to load angular more than once.\n"+
				 		"SPEC: FAILED\n"+
					"...";
		GenericTest parse = parse(s);

		GenericTestFile first = Iterables.getFirst(parse.files, null);
		assertThat(first.path).isEqualTo("FAILED");

		GenericTestCase testCase = Iterables.getFirst(first.testCases, null);
		assertThat(testCase.name).isEqualTo("SOME_NAME");

		assertFailure(testCase);
	}
	
	private void assertSuccess(GenericTestFile genericTestFile, int nrTestCases) {
		assertThat(genericTestFile.path).isEqualTo("SUCCESS");
		assertThat(genericTestFile.testCases).hasSize(nrTestCases);
	}

	private void assertFailure(GenericTestCase testCase) {
		GenericTestFailure failure = testCase.failure;
		assertThat(failure.message).isEqualTo("expected true to be false");
		assertThat(failure.stackTrace).contains(Arrays.asList("AssertionError", "addProperty"));
	}

	private GenericTest parse(String s) throws IOException {
		ByteArrayInputStream source = new ByteArrayInputStream(s.getBytes());
		return GenericTestTapParser.parseTapFile(source);
	}
}
