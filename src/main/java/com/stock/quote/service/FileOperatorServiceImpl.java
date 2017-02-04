package com.stock.quote.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stock.quote.util.Checks;

public class FileOperatorServiceImpl implements IFileOperatorService {
	private final static Logger logger = Logger.getLogger(FileOperatorServiceImpl.class.getName());

	@Override
	public void writeToFile(String fileName, List<String> content, boolean append) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName), append))) {
			for (String line : content) {
				bw.write(line + "\n");
			}
		} catch (IOException e) {
			logger.log(Level.WARNING, "IO exception occurred while writing to file " + fileName
					+ ". And exception is : " + e.getMessage());
		}
	}

	@Override
	public List<String> readFromResource(String fileName) {
		ClassLoader classLoader = getClass().getClassLoader();
		if (Checks.isNull(classLoader.getResource(fileName)))
			return null;
		File inputFile = new File(classLoader.getResource(fileName).getFile());
		return readFromFile(inputFile);
	}

	@Override
	public List<String> readFromOutsideLocation(String fileName) {
		File inputFile = new File(fileName);
		return readFromFile(inputFile);
	}

	private static List<String> readFromFile(File inputFile) {
		List<String> linesInFile = null;
		if (!inputFile.exists())
			return null;
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			linesInFile = new ArrayList<>();
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				linesInFile.add(sCurrentLine);
			}
		} catch (IOException e) {
			logger.log(Level.WARNING, "IO exception occurred while reading from file " + inputFile.getName()
					+ ". And exception is : " + e.getMessage());
		}
		return linesInFile;
	}

}
