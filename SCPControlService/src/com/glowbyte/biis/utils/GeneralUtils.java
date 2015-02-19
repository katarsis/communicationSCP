package com.glowbyte.biis.utils;

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

}
