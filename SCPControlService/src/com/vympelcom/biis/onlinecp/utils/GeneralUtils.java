package com.vympelcom.biis.onlinecp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneralUtils {
	
	public static Date getDateFromString (String dateString) throws ParseException
	{
		SimpleDateFormat formatFromDBWithMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat formatFromDB = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
		Date resultDate;
		if(dateString.matches(".*\\..*"))
		{
			//SQL date string (ISO 9075) 
			String javaTimestamp = "";
			if(dateString.length()>"yyyy-MM-dd HH:mm:ss.SSS".length())
				javaTimestamp = dateString.substring(0, 23);
			else
			{
				javaTimestamp = dateString;
			}
			resultDate =  formatFromDBWithMS.parse(javaTimestamp);
		}
		else 
			resultDate =  formatFromDB.parse(dateString);
		return resultDate;
	}
	
	public static long getDateDifferenceInDay(Date startDay, Date endDay){
		long result = -1;
		long diff = endDay.getTime()-startDay.getTime();
		/*Calendar calendar = Calendar.getInstance();
	    calendar.setTime(diff);
	    result = calendar.get(Calendar.DAY_OF_MONTH); */
		result = diff / (1000 * 60 * 60 * 24);
		return result;
	}

}
