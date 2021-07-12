package com.cybersource.inappsdk.common.utils;

import android.text.TextUtils;

import com.cybersource.inappsdk.common.SDKCurrency;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

/**
 * This class provides static common methods to be used by other classes in the
 * project.
 * 
 * Created by fzubair on 10/7/2015.
 */

public class SDKUtils {

	/** Format of 'date': yyyy-MM-dd */
	private final static String DATE_FORMAT = "yyyy-MM-dd";

	/** Format of 'time': HH:mm AM/PM */
	private final static String TIME_FORMAT = "hh:mm a";

	private final static String DEFAULT_TIMEZONE = "America/Los_Angeles";

	private final static String UTC_TIMEZONE = "UTC";

	private final static String BASIC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	/** Max connection attempts = 5 */
	private final static int CONNECTION_ATTEPTS = 5;

	/** Connection timeout = 10000ms */
	private final static int CONNECTION_TIMEOUT = 10000;

	/** Connection timeout = 30000ms */
	private final static int RECIEVE_DATA_TIMEOUT = 30000;

	/** Port: 80 */
	private final static int PORT_80 = 80;

	/** Port: 443 */
	private final static int PORT_443 = 443;

	/**
	 * This method checks if given class exists basing on its name. If it does
	 * exist it also checks if conforms to a given class type.
	 * 
	 * @param className the name of the class to be checked
	 * @param clazz type for comparison
	 * @return {@code true} when class exists and it is of a given type, {code
	 *         false} otherwise
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean checkIfClassExists(String className, Class clazz) {
		try {
			// check if class exists and if it is instance of given class
			if (clazz.isAssignableFrom(Class.forName(className))) {
				return true;
			}
		} catch (ClassNotFoundException e) {
            // class not found - no need to handle such situation
            e.printStackTrace();
/*            try {
                JarFile jarFile = new JarFile(IDTECH_JAR_FILE_PATH);
                Enumeration enumeration = jarFile.entries();

                URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
                URLClassLoader cl = URLClassLoader.newInstance(urls);

                while (e.hasMoreElements()) {
                    JarEntry je = (JarEntry) e.nextElement();
                    if(je.isDirectory() || !je.getName().endsWith(".class")){
                        continue;
                    }
                    // -6 because of .class
                    String className = je.getName().substring(0,je.getName().length()-6);
                    className = className.replace('/', '.');
                    Class c = cl.loadClass(className);
            }
            catch (IOException e1){
                e.printStackTrace();
            } */
		}

		return false;
	}

	/**
	 * Converts given InputStream to String
	 * 
	 * @param is InputStream
	 * @return String from InputStream
	 */
	public static String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is);
		s.useDelimiter("\\A");
		String result = s.hasNext() ? s.next() : "";
		s.close();
		return result;
	}

	public static String convertTimeInMilisToDate(String timeStamp, String timezone) {
		return getTime(timeStamp, timezone, DATE_FORMAT);
	}

	public static String convertTimeInMilisToTime(String timeStamp, String timezone) {
		return getTime(timeStamp, timezone, TIME_FORMAT);
	}

	private static String getTime(String timeStamp, String timezone, String format) {
		TimeZone utc = TimeZone.getTimeZone(UTC_TIMEZONE);
		long time = stringToLong(timeStamp);
		TimeZone timeZone;
		if (!TextUtils.isEmpty(timezone)) {
			timeZone = TimeZone.getTimeZone(timezone);
		} else {
			timeZone = TimeZone.getTimeZone(DEFAULT_TIMEZONE);
		}
		SimpleDateFormat sdf = new SimpleDateFormat(BASIC_DATE_FORMAT);
		sdf.setTimeZone(timeZone);
		Date date = new Date(time);
		String input = sdf.format(date); // here we have UTC time
		GregorianCalendar cal = new GregorianCalendar(utc);
		// now we try to parse it to local time
		try {
			SimpleDateFormat s = new SimpleDateFormat(BASIC_DATE_FORMAT);
			s.setTimeZone(utc);
			cal.setTime(s.parse(input));
			Date date2 = new Date(cal.getTime().getTime());
			SimpleDateFormat simpleDate = new SimpleDateFormat(format);
			return simpleDate.format(date2);
		} catch (ParseException e) {
			// return UTC time if parse failed
			return input;
		}
	}

	public static String convertToLocalTime(String input) {
		return getTime(input, TIME_FORMAT);
	}

	public static String convertToLocalDate(String input) {
		return getTime(input, DATE_FORMAT);
	}

	private static String getTime(String input, String format) {
		TimeZone utc = TimeZone.getTimeZone(UTC_TIMEZONE);
		SimpleDateFormat f = new SimpleDateFormat(BASIC_DATE_FORMAT);
		f.setTimeZone(utc);
		GregorianCalendar cal = new GregorianCalendar(utc);
		try {
			cal.setTime(f.parse(input));
			Date date = new Date(cal.getTime().getTime());
			SimpleDateFormat simpleDate = new SimpleDateFormat(format);
			return simpleDate.format(date);
		} catch (ParseException e) {
			return input;
		}
	}

	public static long stringToLong(String value) {
		long result = 0;
		if (value != null) {
			try {
				result = Long.parseLong(value);
			} catch (NumberFormatException exception) {
				exception.printStackTrace();
			}
		}
		return result;
	}

	public static HttpsURLConnection getHttpsURLConnection(String urlString, String requestMethod, boolean doOutput)
			throws IOException {
		URL url = new URL(urlString);
		HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
		if (requestMethod != null) {
			urlConnection.setRequestMethod(requestMethod);
		}
		urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
		urlConnection.setReadTimeout(RECIEVE_DATA_TIMEOUT);
		urlConnection.setDoOutput(doOutput);
		urlConnection.setDoInput(true);
		//android.util.Log.d("VMposUtils", "Connection: " + requestMethod + " -to- " + urlString);
		return urlConnection;
	}

	/**
	 * Converts byte array to human readable Hex string.
	 * 
	 * @param data
	 * @return human readable string hex representation of given byte array
	 */
	public static String getHexStringFromBytes(byte[] data) {
		if (data.length <= 0) {
			return null;
		}
		StringBuffer hexString = new StringBuffer();
		String fix = null;
		for (int i = 0; i < data.length; i++) {
			fix = Integer.toHexString(0xFF & data[i]);
			if (fix.length() == 1) {
				fix = "0" + fix;
			}
			hexString.append(fix);
		}
		fix = null;
		fix = hexString.toString();
		return fix;
	}
	
	/**
	 * Converts Strings to BigInteger and checks if the first one is bigger than the
	 * second one. In case of an NumberFormatException it returns
	 * <code>-2</code>
	 * 
	 * @param id1 String id
	 * @param id2 String id
	 * @return <code>-1, 0 or 1</code> or. <code>-2</code> in case of
	 *         exception.
	 */
	public static int compareStringIds(String id1, String id2) {
		
		if (id1 == null || id2 == null) {
			return -2;
		}
		
		try {
			
			BigInteger big1 = new BigInteger(id1);
			BigInteger big2 = new BigInteger(id2);
			return big1.compareTo(big2);
			
		} catch (NumberFormatException ex) {
			return -2;
		}
	}

    public static SDKCurrency getSDKCurrencyFromString(String currencyString){

        switch (currencyString){
            case "USD":
                return SDKCurrency.USD;
            case "CAD":
                return SDKCurrency.CAD;
            case "EUR":
                return SDKCurrency.EUR;
            case "GBP":
                return SDKCurrency.GBP;
            default:
                return null;
        }
    }

    /**
     * Convert BigDecimal amount value to String to send to the Gateway
     * @param value the amount in Big Decimal to be converted
     * @return
     */
    public static String getGatewayAmountStringFromBigDecimal(BigDecimal value){
        BigDecimal amount = value.setScale(2, RoundingMode.CEILING);
        return amount.toPlainString();
    }
	
}