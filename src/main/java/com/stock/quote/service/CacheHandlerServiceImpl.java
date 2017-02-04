package com.stock.quote.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.stock.quote.pojo.StockData;
import com.stock.quote.util.Checks;
import com.stock.quote.util.Constants;
import com.stock.quote.util.Lock;

public class CacheHandlerServiceImpl implements ICacheHandlerService{
	private Gson gson = new Gson();
	private IFileOperatorService fileOperatorService = new FileOperatorServiceImpl();
	private IDataHandlerService dataModelerService = new DataHandlerServiceImpl();

	public CacheHandlerServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void populateCache(Map<String, StockData> stockDataMap) {
		Lock lock = new Lock();
		synchronized (lock) {
			fileOperatorService.writeToFile(Constants.NAME_OF_CACHE_OF_STOCK_DATA,
					dataModelerService.prepareDataForCaching(stockDataMap), false);
		}
	}

	@Override
	public Map<String, StockData> getFromCache() {
		Map<String, StockData> stockDataMap = new HashMap<>();
		List<String> linesInFile = fileOperatorService.readFromOutsideLocation(Constants.NAME_OF_CACHE_OF_STOCK_DATA);
		if (Checks.isNullOrEmpty(linesInFile)) {
			return null;
		}
		for (String line : linesInFile) {
			if (Checks.isNullOrEmpty(line))
				return null;
			String[] keyValuePair = line.split("=");
			if (keyValuePair[0].equals("lastSaveTime")) {
				if (new Date().getTime() - Long.parseLong(keyValuePair[1]) > Constants.MILLIS_IN_A_MINUTE) {
					// invalidate cache
					Lock lock = new Lock();
					synchronized (lock) {
						fileOperatorService.writeToFile(Constants.NAME_OF_CACHE_OF_STOCK_DATA, Arrays.asList(""), false);
					}
					break;
				}
				continue;
			} else {
				stockDataMap.put(keyValuePair[0], gson.fromJson(keyValuePair[1], StockData.class));
			}
		}
		return stockDataMap;
	}
}
