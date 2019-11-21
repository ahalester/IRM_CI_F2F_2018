package de.obopstest.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static Logger LOG = LoggerFactory.getLogger(DateUtil.class);


    public static String getCurrentDateplusDays(int days) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR, days); // <--
        Date tomorrow = cal.getTime();
//        System.out.println(tomorrow);

        String PATTERN="d MMMMMMMMM yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat();
        dateFormat.applyPattern(PATTERN);
        String date1=dateFormat.format(tomorrow);
//        System.out.println(date1);
        return date1;
    }
}
