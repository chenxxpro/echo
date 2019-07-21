package net.nextabc.echo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class Texts {

    public static String requireNonEmpty(String in) {
        if (null == in || in.isEmpty()) {
            throw new IllegalArgumentException("null or empty");
        }
        return in;
    }

    public static String[] split(String template, String separator) {
        final List<String> out = new ArrayList<>();
        int index = template.indexOf(separator);
        int preIndex = 0;
        while (index != -1) {
            if (preIndex != index) {
                out.add(template.substring(preIndex, index));
            }
            index++;
            preIndex = index;
            index = template.indexOf(separator, index);
        }
        if (preIndex < template.length()) {
            out.add(template.substring(preIndex));
        }
        return out.toArray(new String[0]);
    }
}
