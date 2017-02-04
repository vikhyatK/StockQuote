package com.stock.quote.pojo;

import com.stock.quote.util.Checks;

/**
 * This class holds data of a particular stock fetched from yahoo api
 * 
 * @author Vikhyat
 *
 */
public class StockData {

	private String symbol;
	private String YearLow;
	private String YearHigh;
	private String LastTradePriceOnly;
	private String OneyrTargetPrice;

	public StockData() {
		super();
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getYearLow() {
		return YearLow;
	}

	public void setYearLow(String yearLow) {
		YearLow = yearLow;
	}

	public String getYearHigh() {
		return YearHigh;
	}

	public void setYearHigh(String yearHigh) {
		YearHigh = yearHigh;
	}

	public String getLastTradePriceOnly() {
		return LastTradePriceOnly;
	}

	public void setLastTradePriceOnly(String lastTradePriceOnly) {
		LastTradePriceOnly = lastTradePriceOnly;
	}

	public String getOneyrTargetPrice() {
		return OneyrTargetPrice;
	}

	public void setOneyrTargetPrice(String oneyrTargetPrice) {
		OneyrTargetPrice = oneyrTargetPrice;
	}

	public String toJsonString() {
		StringBuilder sb = new StringBuilder("{\"symbol\":\"" + symbol + "\",");
		sb.append("\"LastTradePriceOnly\":\"")
		.append((Checks.isNullOrEmpty(LastTradePriceOnly)) ? "-1\"," : LastTradePriceOnly + "\",")
		.append("\"OneyrTargetPrice\":\"")
		.append((Checks.isNullOrEmpty(OneyrTargetPrice)) ? "-1\"," : OneyrTargetPrice + "\",")
		.append("\"YearHigh\":\"")
				.append((Checks.isNullOrEmpty(YearHigh)) ? "-1\"," : YearHigh + "\",")
		.append("\"YearLow\":\"")
				.append((Checks.isNullOrEmpty(YearLow)) ? "-1\"}" : YearLow + "\"}");
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(symbol + ",");
		sb.append((Checks.isNullOrEmpty(LastTradePriceOnly)) ? "-1," : LastTradePriceOnly + ",")
				.append((Checks.isNullOrEmpty(OneyrTargetPrice)) ? "-1," : OneyrTargetPrice + ",")
				.append((Checks.isNullOrEmpty(YearHigh)) ? "-1," : YearHigh + ",")
				.append((Checks.isNullOrEmpty(YearLow)) ? "-1" : YearLow);
		return sb.toString();
	}
}
