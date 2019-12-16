package com.example.worldnewsapp.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppConstants {
    public static final String API_KEY = "";
    public static Date parse(String input) throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
        if (input.endsWith("Z")){
            input = input.substring(0, input.length() -1) +"GMT-00:00";
        }else{
            int inset = 6;
            String startTime = input.substring(0,input.length() - inset);
            String endTime = input.substring(input.length() - inset, input.length());
            input = startTime + "GMT" + endTime;

        }
        return df.parse(input);
    }

}
