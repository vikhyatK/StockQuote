package com.stock.quote.util;

/**
 * This class is a utility class to perform general validations and utility tasks on objects
 * @author Vikhyat
 *
 */
public class Checks {

	public static <T> boolean isNull(T obj){
		return (obj == null) ? true : false;
	}
	
	public static boolean isNullOrEmpty(String str){
		return (str == null || str.equals("")) ? true : false;
	}
	
	public static <T> boolean isNullOrEmpty(Iterable<T> itr) {
		return (itr == null || itr.iterator().hasNext() == false) ? true : false;
	}
}
