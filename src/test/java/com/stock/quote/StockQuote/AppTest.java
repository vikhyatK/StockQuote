package com.stock.quote.StockQuote;

import java.io.File;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.stock.quote.pojo.StockData;
import com.stock.quote.quote.StockQuoteFetcher;
import com.stock.quote.service.CacheHandlerServiceImpl;
import com.stock.quote.service.ICacheHandlerService;
import com.stock.quote.util.Constants;

/**
 * Unit test for StockQuote App.
 */
public class AppTest {

    /**
     * Test if the csv file gets created
     */
	@Test
    public void testExportedCSVFileExists()
    {
    	StockQuoteFetcher stockQuoteFetcher = new StockQuoteFetcher();
    	stockQuoteFetcher.performTask();
    	File file = new File(Constants.NAME_OF_CSV_FILE);
        Assert.assertTrue(file.exists());
    }
    
    /**
     * Test if the data is populated in the cache
     */
    @Test
    public void testDataInCacheExists(){
    	ICacheHandlerService cacheHandlerService = new CacheHandlerServiceImpl();
    	Map<String, StockData> stockDataFromCache = cacheHandlerService.getFromCache();
    	Assert.assertNotNull(stockDataFromCache);
    	Assert.assertNotNull(stockDataFromCache.size()>1); // as it contains time of last save in first row
    }
}
