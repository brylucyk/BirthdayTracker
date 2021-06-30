package com.example.blucyk.birthdaytracker;

import java.text.DateFormatSymbols;

public class Birthday {

    private int _birthdayId;
    private String _birthdayName;
    private int _birthdayMonth;
    private int _birthdayDay;
    private String _birthdayMonthName;

    public Birthday(int birthdayId, String birthdayName, int birthdayMonth, int birthdayDay) {
        this._birthdayId = birthdayId;
        this._birthdayName = birthdayName;
        this._birthdayMonth = birthdayMonth;
        this._birthdayDay = birthdayDay;
        this._birthdayMonthName = convertMonthNumToName(birthdayMonth);
    }

    public String convertMonthNumToName(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public String getBirthdayName() { return this._birthdayName; }
    public String toString() {
        return _birthdayMonthName + " " + _birthdayDay;
    }
}
