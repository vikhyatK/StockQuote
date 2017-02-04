package com.stock.quote.util;

public class Constants {
	public static final long MILLIS_IN_A_MINUTE = 60000L;
	public static final String NAME_OF_CACHE_OF_STOCK_DATA = System.getProperty("user.home") + "/CacheStockData.txt";
	public static final String NAME_OF_STOCK_SYMBOLS = "files/Stocks.txt";
	public static final String NAME_OF_CSV_FILE = System.getProperty("user.home") + "/StocksData.csv";
	public static final String CSV_FILE_HEADER = "Stock Symbol,Current Price,Year Target Price,Year High,Year Low";
}
