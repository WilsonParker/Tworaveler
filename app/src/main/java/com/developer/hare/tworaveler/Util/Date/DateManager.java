package com.developer.hare.tworaveler.Util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import com.developer.hare.tworaveler.Util.Log_HR;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hare on 2017-08-03.
 */

public class DateManager {
    private static final DateManager ourInstance = new DateManager();
    private Date date = new Date();
    private DateFormat dateFormat;
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePickerDialog;
    private String stringDate;

    private TextView textView;
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            stringDate = year + "년" + (monthOfYear + 1) + "월" + dayOfMonth + "일";
            textView.setText(stringDate);
        }
    };

    public static DateManager getInstance() {
        return ourInstance;
    }

    private DateManager() {
    }

    public String getNow(int dateType) {
        dateFormat = DateFormat.getDateInstance(dateType);
        return dateFormat.format(date);
    }

    public String getDate(String pattern) {
        simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public int getIntegerDate(String pattern) {
        simpleDateFormat = new SimpleDateFormat(pattern);
        return Integer.parseInt(simpleDateFormat.format(date));
    }

    public void getDateTime(Context context, TextView textView) {
        this.textView = textView;
        datePickerDialog = new DatePickerDialog(context, onDateSetListener, getIntegerDate("yyyy"), getIntegerDate("MM") - 1, getIntegerDate("dd"));
        datePickerDialog.show();
    }

    public Date parseDate(String date, String pattern) {
        Date result = null;
        try {
            result = new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            Log_HR.log(getClass(), "parseDate(String, String)", "ParseException", e);
        }
        return result;
    }

    public String formatDate(long date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    private Calendar getCalendarDate(int[] iArr) {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.YEAR, iArr[0]);
        date.add(Calendar.MONTH, iArr[1]);
        date.add(Calendar.DAY_OF_MONTH, iArr[2]);
        return date;
    }

    public int[] getTimeArr(Date date) {
        int[] is = {Integer.parseInt(formatDate(date.getTime(), "yyyy")),Integer.parseInt(formatDate(date.getTime(), "MM")),Integer.parseInt(formatDate(date.getTime(), "dd"))};
        return is;
    }
}
