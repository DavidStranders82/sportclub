package com.davidstranders.sportclub.utils;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dell on 5-7-2017.
 */
public class Parsers {

    public static byte[] parseImage(String filename) {
        byte[] data = null;
        try {
            InputStream is = ClassLoader.class.getResourceAsStream("/static/images/"+ filename + ".jpg");
            data = ByteStreams.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Date parseDate(String ddmmyyyy) {
        java.util.Date utilDate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return utilDate = formatter.parse(ddmmyyyy);
        } catch (ParseException e) {
            e.printStackTrace();
            return utilDate;
        }
    }

    public static Date parseDateTime(String dateTime) {
        java.util.Date utilDate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            return utilDate = formatter.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return utilDate;
        }
    }
}
