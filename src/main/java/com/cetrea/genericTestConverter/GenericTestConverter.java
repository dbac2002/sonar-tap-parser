package com.cetrea.genericTestConverter;

import static com.cetrea.genericTestConverter.GenericTestTapParser.parseTapFile;
import static java.nio.file.Files.newInputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;
import com.cetrea.genericTestConverter.model.GenericTest;

public class GenericTestConverter {
	@Parameter(names = { "-o",
			"--output" }, converter = PathConverter.class, description = "the generic test plugin xml for output")
	private Path output;

	@Parameter(names = { "-i", "--input" }, converter = PathConverter.class, description = "the tap file to parse")
	private Path input;

	public static void main(String[] args) throws IOException, JAXBException {
		GenericTestConverter converter = new GenericTestConverter();

		JCommander jCommander = new JCommander(converter, args);

		if (converter.output == null || converter.input == null) {
			jCommander.setProgramName(GenericTestConverter.class.getSimpleName());
			jCommander.usage();
			System.exit(-1);
		}

		converter.convert();
	}

	private void convert() throws IOException, JAXBException {
		writeGenericXml(parseTapFile(newInputStream(input)));
	}

	private void writeGenericXml(GenericTest test) throws JAXBException, IOException {
		if (test != null) {
			JAXBContext context = JAXBContext.newInstance(GenericTest.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(test, Files.newOutputStream(output));
		}
	}
}
