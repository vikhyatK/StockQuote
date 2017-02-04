package com.stock.quote.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.stock.quote.pojo.StockData;
import com.stock.quote.util.Checks;
import com.stock.quote.util.Constants;

public class DataHandlerServiceImpl implements IDataHandlerService {

	private IFileOperatorService fileOperatorService = new FileOperatorServiceImpl();

	@Override
	public List<String> prepareDataForCaching(Map<String, StockData> stockDataMap) {
		List<String> list = new ArrayList<>();
		list.add("lastSaveTime=" + new Date().getTime());
		for (Entry<String, StockData> entry : stockDataMap.entrySet()) {
			if (!Checks.isNull(entry.getValue()))
				list.add(entry.getKey() + "=" + entry.getValue().toJsonString());
		}
		return list;
	}

	@Override
	public List<String> getStockSymbols(String nameOfStockSymbols) {
		return fileOperatorService.readFromResource(Constants.NAME_OF_STOCK_SYMBOLS);
	}

	@Override
	public void exportDataToCSVFile(Map<String, StockData> stockDataMap) {
		fileOperatorService.writeToFile(Constants.NAME_OF_CSV_FILE, prepareDataForExport(stockDataMap), false);
		System.out.println("Data exported to file : " + Constants.NAME_OF_CSV_FILE);
	}

	private List<String> prepareDataForExport(Map<String, StockData> stockDataMap) {
		List<String> list = new ArrayList<>();
		for (Entry<String, StockData> entry : stockDataMap.entrySet()) {
			if (!Checks.isNull(entry.getValue())) {
				list.add(entry.getValue().toString());
			} else {
				list.add(entry.getKey() + ",-1,-1,-1,-1");
			}
		}
		Collections.sort(list);
		list.add(0, Constants.CSV_FILE_HEADER);
		return list;
	}
}
