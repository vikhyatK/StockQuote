package com.stock.quote.service;

import java.util.List;

public interface IFileOperatorService {

	void writeToFile(String fileName, List<String> content, boolean append);

	List<String> readFromResource(String fileName);

	List<String> readFromOutsideLocation(String fileName);
}
