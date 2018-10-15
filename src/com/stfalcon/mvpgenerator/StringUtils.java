package com.stfalcon.mvpgenerator;

final class StringUtils
{
    static String camelCaseToSnakeCase(final String inputText) {
        final String regex = "([a-z])([A-Z]+)";
        final String replacement = "$1_$2";
        return inputText.replaceAll(regex, replacement).toLowerCase();
    }
}
