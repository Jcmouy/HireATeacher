package com.ejemplo.appdocente.Util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class FormatText {

    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}
