package com.feng.server.utils;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Years;
import sun.rmi.runtime.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author f
 * @date 2023/5/2 21:15
 */
@Slf4j
public class GetAgeUtil {

    /**
     * 根据出生年月获取年龄
     * @param yearMonthDat date
     * @return             age
     */
    public static int getAge(String yearMonthDat) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthday = sdf.parse(yearMonthDat);
            Years years = Years.yearsBetween(new DateTime(birthday), DateTime.now());
            return years.getYears();
        } catch (ParseException e) {
            log.error("计算年龄出错");
            return 0;
        }
    }
}
