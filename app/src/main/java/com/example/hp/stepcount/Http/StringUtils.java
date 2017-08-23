package com.example.hp.stepcount.Http;

import java.util.Random;
import java.util.Vector;

/**
 * Created by bin on 10/02/2017.
 */

public class StringUtils {

    public static boolean isEmpty(String str){
        if(null == str){
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    public static boolean isBlank(String str){
        if(isEmpty(str)){
            return true;
        }
        if(str.length()<1){
            return true;
        }
        return false;
    }
    public static boolean isNotBlank(String str){
        return !isBlank(str);
    }

    /**
     */
    public static boolean isNumber(String val){
        try {
            Double.parseDouble(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean parseBoolean(String value){
        if(isBlank(value)){
            return false;
        } else if("T".equalsIgnoreCase(value) || "Y".equalsIgnoreCase(value) || "TRUE".equalsIgnoreCase(value)){
            return true;
        } else if("F".equalsIgnoreCase(value) || "N".equalsIgnoreCase(value) || "FALSE".equalsIgnoreCase(value)){
            return false;
        } else {
            return Boolean.parseBoolean(value);
        }
    }

    public static String substringBeforeLast(String localPath, String string) {
        if(localPath == null){
            return null;
        }
        int end = localPath.lastIndexOf(string);
        if(end == -1){
            return localPath;
        }
        return localPath.substring(0, end);
    }
    public static String upperFirst(String source){
        if(isBlank(source)){
            return null;
        }
        String first = (source.charAt(0)+"").toUpperCase();
        return first + source.substring(1);
    }
    public static String toE6String(String n){

        return null;
    }

    public static String fromE6String(String n){
        return null;
    }

    public static boolean startsWithIgnoreCase(String s, String star){
        return s.toLowerCase().startsWith(star.toLowerCase());
    }

    public static void append(StringBuffer dest, StringBuffer s){
        dest.append(s.toString());
    }

    public static void append(StringBuffer dest, String s, int start, int length){
        if(s == null || length == 0){
            return;
        }
        dest.append(s.substring(start, start+length));
    }

    public static void append(StringBuffer dest, byte[] buffer, int start, int length){
        if(buffer == null || length == 0){
            return;
        }
        dest.append(new String(buffer, start, length));
    }

    public static String cleanText(String result) {
        StringBuffer ret = new StringBuffer();
        byte[] bytes = result.getBytes();
        int i = 0;
        for(i = 0; i < bytes.length; i++) {
            if(bytes[i] < 128 && bytes[i] > 0) {
                append(ret, bytes, i, 1);
            }
        }
        return ret.toString();
    }

    public static String[] strSplit(String str, String[] sep, int offset) {
        Vector result = new Vector();
        while(offset < str.length()) {
            int findpos = -1;
            int seplen = 0;
            for(int i = 0; i < sep.length; i ++) {
                int pos = str.indexOf(sep[i], offset);
                if(pos >= 0 && (findpos < 0 || findpos > pos || (findpos == pos && seplen < sep[i].length()))) {
                    findpos = pos;
                    seplen = sep[i].length();
                }
            }
            if(findpos >= 0) {
                if(findpos > offset) {
                    result.addElement(str.substring(offset, findpos));
                }
                offset = findpos + seplen;
            }else if(offset < str.length()){
                result.addElement(str.substring(offset));
                offset = str.length();
            }
        }
        String ret[] = new String[result.size()];
        for(int i = 0; i < result.size(); i ++) {
            ret[i] = (String)result.elementAt(i);
        }
        return ret;
    }

    public static String getRandomNumber(int length) {
        if (length < 1) {
            return "";
        }
        String[] arr = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        StringBuffer sbf = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            Random r = new Random();
            int ra = r.nextInt(arr.length);
            sbf.append(arr[ra]);
        }
        return sbf.toString();
    }


    public static String getRandomStr(int length) {
        if (length < 1) {
            return "";
        }
        String[] arr = { "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "m",
                "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "2", "3", "4", "5", "6", "7", "8", "9" };
        StringBuffer sbf = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            Random r = new Random();
            int ra = r.nextInt(arr.length);
            sbf.append(arr[ra]);
        }
        return sbf.toString();
    }
}