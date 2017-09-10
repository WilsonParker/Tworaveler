package com.developer.hare.tworaveler.Util.Date;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.developer.hare.tworaveler.Util.LogManager;

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
    private TimePickerDialog timePickerDialog;
    private View.OnClickListener onClickListener;
    private String stringDate, stringTime;
    private final int StartYear = 1900;

    private TextView textView;
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            stringDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            textView.setText(stringDate);
            if (onClickListener != null)
                onClickListener.onClick(null);
        }
    };

    private TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            stringTime = String.format("%02d:%02d", i, i1);
            textView.setText(stringTime);
            if (onClickListener != null)
                onClickListener.onClick(null);
        }
    };

    public static DateManager getInstance() {
        return ourInstance;
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

    public void getDateYMD(Context context, TextView textView) {
        onClickListener = null;
        this.textView = textView;
        datePickerDialog = new DatePickerDialog(context, onDateSetListener, getIntegerDate("yyyy"), getIntegerDate("MM") - 1, getIntegerDate("dd"));
        datePickerDialog.show();
    }

    public void getDateYMD(Context context, TextView textView, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.textView = textView;
        datePickerDialog = new DatePickerDialog(context, onDateSetListener, getIntegerDate("yyyy"), getIntegerDate("MM") - 1, getIntegerDate("dd"));
        datePickerDialog.show();
    }

    public void getDateTime(Context context, TextView textView) {
        onClickListener = null;
        this.textView = textView;
        timePickerDialog = new TimePickerDialog(context, onTimeSetListener, getIntegerDate("HH"), getIntegerDate("mm"), true);
        timePickerDialog.show();
    }

    public void getDateTime(Context context, TextView textView, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.textView = textView;
        timePickerDialog = new TimePickerDialog(context, onTimeSetListener, getIntegerDate("HH"), getIntegerDate("mm"), true);
        timePickerDialog.show();
    }

    public Date parseDate(String date, String pattern) {
        Date result = null;
        try {
            result = new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            LogManager.log(getClass(), "parseDate(String, String)", "ParseException", e);
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
        int[] is = {Integer.parseInt(formatDate(date.getTime(), "yyyy")), Integer.parseInt(formatDate(date.getTime(), "MM")), Integer.parseInt(formatDate(date.getTime(), "dd"))};
        return is;
    }

    public boolean compareDate(String start, String end, String pattern) {
        Date startDate = parseDate(start, pattern);
        Date endDate = parseDate(end, pattern);
        return startDate.getTime() < endDate.getTime();
    }
}
