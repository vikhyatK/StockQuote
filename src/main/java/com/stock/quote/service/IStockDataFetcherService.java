package com.stock.quote.service;

import java.util.List;
import java.util.Map;

import com.stock.quote.exceptions.MyException;
import com.stock.quote.pojo.StockData;

public interface IStockDataFetcherService {

	Map<String, StockData> getStockQuote(List<String> stockSymbolList) throws MyException;

}
