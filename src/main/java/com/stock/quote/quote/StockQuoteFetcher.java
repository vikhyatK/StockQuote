package com.stock.quote.quote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stock.quote.exceptions.MyException;
import com.stock.quote.pojo.StockData;
import com.stock.quote.service.CacheHandlerServiceImpl;
import com.stock.quote.service.DataHandlerServiceImpl;
import com.stock.quote.service.ICacheHandlerService;
import com.stock.quote.service.IDataHandlerService;
import com.stock.quote.service.IStockDataFetcherService;
import com.stock.quote.service.StockDataFetcherServiceImpl;
import com.stock.quote.util.Checks;
import com.stock.quote.util.Constants;

/**
 * This class fetches quote for a stock symbol(given in a file) from yahoo api
 * and records the result in a csv file with columns Stock Symbol, Current
 * Price, Year Target Price, Year High, Year Low.
 * 
 * @author Vikhyat
 */
public class StockQuoteFetcher {
	private final static Logger logger = Logger.getLogger(StockQuoteFetcher.class.getName());

	private IDataHandlerService dataModelerService = new DataHandlerServiceImpl();
	private ICacheHandlerService cacheHandlerservice = new CacheHandlerServiceImpl();
	private IStockDataFetcherService stockDataFetcherService = new StockDataFetcherServiceImpl();

	public static void main(String[] args) {
		StockQuoteFetcher stockQuoteFetcher = new StockQuoteFetcher();
		stockQuoteFetcher.performTask();
	}

	public void performTask() {
		List<String> stockSymbolsList = dataModelerService.getStockSymbols(Constants.NAME_OF_STOCK_SYMBOLS);
		Map<String, StockData> stockDataMapFromCache = cacheHandlerservice.getFromCache();
		Map<String, StockData> stockDataMap = new HashMap<>();
		List<String> stockSymbolsToBeFetchedFromAPI = new ArrayList<>();
		if(Checks.isNullOrEmpty(stockSymbolsList)) {
			logger.log(Level.SEVERE, "The input file Stocks.txt cannot be found");
			return;
		}
		for (String stockSymbol : stockSymbolsList) {
			StockData stockData = null;
			if (!Checks.isNull(stockDataMapFromCache)) {
				stockData = stockDataMapFromCache.get(stockSymbol);
			}
			if (Checks.isNull(stockData)) {
				stockSymbolsToBeFetchedFromAPI.add(stockSymbol);
			} else {
				stockDataMap.put(stockSymbol, stockData);
			}
		}
		try {
			Map<String, StockData> fetchStockDataMap = stockDataFetcherService.getStockQuote(stockSymbolsToBeFetchedFromAPI);
			if(!Checks.isNull(fetchStockDataMap)){
				stockDataMap.putAll(fetchStockDataMap);
			}
		} catch (MyException e) {
			logger.log(Level.INFO, "Didn't get data from yahoo api for symbols " + stockSymbolsToBeFetchedFromAPI
					+ ". And error is :" + e.getMessage());
		}
		cacheHandlerservice.populateCache(stockDataMap);
		dataModelerService.exportDataToCSVFile(stockDataMap);
	}
}