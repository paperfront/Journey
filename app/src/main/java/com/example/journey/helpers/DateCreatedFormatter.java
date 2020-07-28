package com.example.journey.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCreatedFormatter {
    private static SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    public static String formatDate(Date date) {
        return formatter.format(date);
    }
}
