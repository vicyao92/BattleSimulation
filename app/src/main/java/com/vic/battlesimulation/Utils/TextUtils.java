package com.vic.battlesimulation.Utils;

public class TextUtils {
    public static String textFormat(String str){
        String temp;
        if (str.equals("")){
            temp = "0";
        }else {
            temp = str;
        }
        return temp;
    }
}
