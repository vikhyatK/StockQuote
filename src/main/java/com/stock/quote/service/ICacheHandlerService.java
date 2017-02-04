package com.stock.quote.service;

import java.util.Map;

import com.stock.quote.pojo.StockData;

public interface ICacheHandlerService {
	public void populateCache(Map<String, StockData> stockDataMap);

	public Map<String, StockData> getFromCache();
}
