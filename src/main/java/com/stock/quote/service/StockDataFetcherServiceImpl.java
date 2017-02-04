package com.stock.quote.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.stock.quote.exceptions.MyException;
import com.stock.quote.pojo.StockData;

public class StockDataFetcherServiceImpl implements IStockDataFetcherService {
	private final static Logger logger = Logger.getLogger(StockDataFetcherServiceImpl.class.getName());
	private Gson gson = new Gson();

	public StockDataFetcherServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<String, StockData> getStockQuote(List<String> stockSymbolList) throws MyException {
		Map<String, StockData> stockDataMap = new HashMap<>();
		int batchSize = 20;
		int offset = 0;
		List<String> concatenatedStockSymbols = new ArrayList<>();
		for (int i = 0; i < stockSymbolList.size(); i = i + batchSize) {
			StringBuilder sb = new StringBuilder();
			offset = i + batchSize;
			for (int j = i; j < offset; j++) {
				if (stockSymbolList.size() < i + batchSize && stockSymbolList.size() - j < batchSize) {
					offset = stockSymbolList.size();
					sb.append(stockSymbolList.get(j) + ",");
				} else {
					sb.append(stockSymbolList.get(j) + ",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			concatenatedStockSymbols.add(sb.toString());
		}
		for (String stockSymbols : concatenatedStockSymbols) {
			try {
				URL url = new URL(
						"https://query.yahooapis.com/v1/public/yql?q=select%20symbol,LastTradePriceOnly,OneyrTargetPrice,YearHigh,YearLow%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22"
								+ stockSymbols
								+ "%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new MyException("Failed : HTTP error code : " + conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output;
				StockData stockData = null;
				while ((output = br.readLine()) != null) {
					JsonElement jsonElement = gson.fromJson(output, JsonElement.class);
					JsonElement qouteJsonElement = jsonElement.getAsJsonObject().get("query").getAsJsonObject()
							.get("results").getAsJsonObject().get("quote");
					if (qouteJsonElement.isJsonArray()) {
						JsonArray jsonArray = qouteJsonElement.getAsJsonArray();
						Iterator<JsonElement> itr = jsonArray.iterator();
						while (itr.hasNext()) {
							stockData = gson.fromJson(itr.next(), StockData.class);
							stockDataMap.put(stockData.getSymbol(), stockData);
							logger.log(Level.INFO, "Downloaded data for : " + stockData);
						}
					} else if (qouteJsonElement.isJsonObject()) {
						stockData = gson.fromJson(qouteJsonElement, StockData.class);
						stockDataMap.put(stockData.getSymbol(), stockData);
					}
				}
				conn.disconnect();
			} catch (MalformedURLException e) {
				logger.log(Level.INFO, "Data can't be fetched due to malformed URL for stocks : " + stockSymbols
						+ ". And the exception is: " + e.getMessage());
			} catch (IOException e) {
				logger.log(Level.INFO, "Data can't be fetched due to IO exception for stocks : " + stockSymbols
						+ ". And the exception is: " + e.getMessage());
			}
		}
		return stockDataMap;
	}
}
