package com.stfalcon.mvpgenerator;


public final class StringUtils {
    public static String camelCaseToSnakeCase(final String inputText) {
        final String regex = "([a-z])([A-Z]+)";
        final String replacement = "$1_$2";
        return inputText.replaceAll(regex, replacement).toLowerCase();
    }
    public static boolean isEmpty(String content){
        if(content==null||content.equals("")){
            return true;
        }else{
            return false;
        }
    }

}
