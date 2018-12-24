package com.stfalcon.mvpgenerator;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InjectFactoryUtils {
    public static void appendSubComponent(String filepath, String[] importStrs, String subComponentClazzStr, String bindMethodStr) {
        try {
            FileInputStream fis = new FileInputStream(filepath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int length;
            byte[] buffer = new byte[1024];
            while ((length = fis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            String content = new java.lang.String(bos.toByteArray(), "utf-8");
            StringBuilder sb = new StringBuilder(content);

            Pattern pattern = Pattern.compile("@\\s*Module\\s*\\(\\s*subcomponents[^}]+([^)]+)");
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                String string = matcher.group(0);
                String includeStr = matcher.group(1);
                String subcomponentStr = string.replace(includeStr, "").replaceAll("\\s", "");
                if (subcomponentStr.endsWith(",")) {
                    subcomponentStr = subcomponentStr.substring(0, subcomponentStr.length() - 1);
                }
                if (subcomponentStr.endsWith("{")) {
                    subcomponentStr += subComponentClazzStr;
                } else {
                    subcomponentStr += "," + subComponentClazzStr;
                }
                String result = subcomponentStr + includeStr;

                result = result.replaceAll("\\s", "").replace("=", " = ").replace(",", ", ");

                sb.replace(matcher.start(), matcher.end(), result);

                Pattern importPattern = Pattern.compile("import[^;]+;");
                Matcher m = importPattern.matcher(sb.toString());
                int lastImportEnd = 0;
                while (m.find()) {
                    lastImportEnd = m.end();
                }
                for (String str : importStrs) {
                    String newStr = "\r\nimport " + str + ";";
                    sb.insert(lastImportEnd, newStr);
                    lastImportEnd += newStr.length();
                }


                int index = sb.lastIndexOf("}");
                sb.insert(index, bindMethodStr + "\r\n");
                FileOutputStream fos = new FileOutputStream(filepath);
                fos.write(sb.toString().getBytes());
            }
        } catch (Exception e) {
            throw new RuntimeException("inject factory file handle fail " + e.getMessage());
        }
    }
}
