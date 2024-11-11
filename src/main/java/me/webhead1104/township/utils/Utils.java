package me.webhead1104.township.utils;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Utils {

    public static List<Integer> thing(String e, int startAt) {
        List<Integer> list = new ArrayList<>();
        //1x3 is hightXwidth
        switch (e.toLowerCase()) {
            case "2x2" -> {
                list.add(startAt);
                list.add(startAt + 1);
                list.add(startAt + 9);
                list.add(startAt + 10);
            }
            case "3x3" -> {
                list.add(startAt);
                list.add(startAt + 1);
                list.add(startAt + 2);
                list.add(startAt + 9);
                list.add(startAt + 10);
                list.add(startAt + 11);
                list.add(startAt + 18);
                list.add(startAt + 19);
                list.add(startAt + 20);
            }
            case "4x4" -> {
                list.add(startAt);
                list.add(startAt + 1);
                list.add(startAt + 2);
                list.add(startAt + 3);
                list.add(startAt + 9);
                list.add(startAt + 10);
                list.add(startAt + 11);
                list.add(startAt + 12);
                list.add(startAt + 18);
                list.add(startAt + 19);
                list.add(startAt + 20);
                list.add(startAt + 21);
            }
            case "1x3" -> {
                list.add(startAt);
                list.add(startAt + 1);
                list.add(startAt + 2);
            }
        }
        return list;
    }

    public static String thing2(String string) {
        return StringUtils.capitalize(string.replaceAll("_", " "));
    }
}
