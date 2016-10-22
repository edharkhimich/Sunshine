
package com.example.appleeeee.myapplication.model;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Forecast {

    private String code;
    private String date;
    private String day;
    private String high;
    private String low;
    private String text;
    private DateTime dt;
    private DateTimeFormatter formatter;
    private DateTimeFormatter formatter2;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
//        LocalTime localTime = LocalTime.now();
//        dt = new DateTime();
//        formatter = DateTimeFormat
//                .forPattern("dd MMM yyyy");
//        dt = formatter.parseDateTime(date);
//
//        formatter2 = DateTimeFormat.forPattern("d MMMM Z");
//        dt = formatter2.parseDateTime(date.replace(""))
//        date = formatter2.print(dt);
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
