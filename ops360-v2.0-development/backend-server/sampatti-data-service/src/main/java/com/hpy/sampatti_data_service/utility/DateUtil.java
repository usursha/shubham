package com.hpy.sampatti_data_service.utility;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateUtil {
	
	public static String formatLastUpdatedDateTime(String dateStr, String timeStr) throws Exception {
        // Convert date string to Date object
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateStr);

        // Format date to desired format
        SimpleDateFormat desiredFormat = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = desiredFormat.format(date);

        // Combine formatted date and time
        return formattedDate + "," + timeStr;
    }

}
