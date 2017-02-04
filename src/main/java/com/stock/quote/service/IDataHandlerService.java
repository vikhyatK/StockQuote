package com.stock.quote.service;

import java.util.List;
import java.util.Map;

import com.stock.quote.pojo.StockData;

public interface IDataHandlerService {

	List<String> prepareDataForCaching(Map<String, StockData> stockDataMap);

	List<String> getStockSymbols(String nameOfStockSymbols);

	void exportDataToCSVFile(Map<String, StockData> stockDataMap);

}
