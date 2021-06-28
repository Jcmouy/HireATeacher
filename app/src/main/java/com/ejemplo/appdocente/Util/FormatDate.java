package com.ejemplo.appdocente.Util;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.room.TypeConverter;

public class FormatDate {

    public Date formatDateFromDB(String textDate) {
        Date date = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = inputFormat.parse(textDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public String formatDateToDB(Date dateSelected) {
        String dateString = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateString = inputFormat.format(dateSelected);
        return dateString;
    }

    public String dateToString(Date date) {
        String formattedDate = "";
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm dd/MM");
        formattedDate = outputFormat.format(date);

        return formattedDate;
    }

    public String doubleMoneyToString(double doubleMoney) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("USD"));
        return format.format(doubleMoney);
    }

    public Long hourMinutesToMillis(String hour, String minutes) {
        Long hourMillis = TimeUnit.HOURS.toMillis(Long.parseLong(hour));
        Long minuteMillis =TimeUnit.MINUTES.toMillis(Long.parseLong(minutes));
        return hourMillis + minuteMillis;
    }

    public Date getDateCalendar (Calendar calendar, int hour , int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    public LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }
}
